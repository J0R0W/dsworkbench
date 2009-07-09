/*
 * TribesPlannerStartFrame.java
 *
 * Created on 9. Juni 2008, 15:54
 */
package de.tor.tribes.ui;

import de.tor.tribes.io.DataHolder;
import de.tor.tribes.io.DataHolderListener;
import de.tor.tribes.io.ServerManager;
import de.tor.tribes.types.Tribe;
import de.tor.tribes.util.Constants;
import de.tor.tribes.util.GlobalOptions;
import de.tor.tribes.util.Skin;
import de.tor.tribes.util.tag.TagManager;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import de.tor.tribes.ui.renderer.ColorCellRenderer;
import de.tor.tribes.io.UnitHolder;
import de.tor.tribes.php.DatabaseInterface;
import de.tor.tribes.types.Tag;
import de.tor.tribes.ui.editors.ColorChooserCellEditor;
import de.tor.tribes.util.ServerChangeListener;
import de.tor.tribes.types.Village;
import de.tor.tribes.ui.renderer.MapRenderer;
import de.tor.tribes.util.JOptionPaneHelper;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Comparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.java.dev.colorchooser.ColorChooser;

/**
 * @TODO (1.X) Integrate browser access for linux
 * @TODO (1.X) Shortcuts editable
 * @TODO (DIFF) Add option to change shown troops of popup
 * @TODO (DIFF) Add setting to set max. value of troop density
 * @TODO (DIFF) Move tags to own view
 * @author Jejkal
 */
public class DSWorkbenchSettingsDialog extends javax.swing.JDialog implements
        DataHolderListener,
        ServerChangeListener {

    private static Logger logger = Logger.getLogger("SettingsDialog");
    private static DSWorkbenchSettingsDialog SINGLETON = null;
    private boolean updating = false;
    private Proxy webProxy;
    private boolean INITIALIZED = false;
    private ColorChooser mTagColorChooser = null;

    public static synchronized DSWorkbenchSettingsDialog getSingleton() {
        if (SINGLETON == null) {
            SINGLETON = new DSWorkbenchSettingsDialog();
        }

        return SINGLETON;
    }

    /** Creates new form TribesPlannerStartFrame */
    DSWorkbenchSettingsDialog() {
        initComponents();

        // <editor-fold defaultstate="collapsed" desc=" General Layout ">

        //general layout
        jCreateAccountDialog.pack();
        jChangePasswordDialog.pack();
        getContentPane().setBackground(Constants.DS_BACK);
        jCreateAccountDialog.getContentPane().setBackground(Constants.DS_BACK);
        setAlwaysOnTop(true);
        mTagColorChooser = new ColorChooser();
        jTroopDensitySelectionDialog.pack();
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Skin Setup">
        DefaultComboBoxModel model = new DefaultComboBoxModel(GlobalOptions.getAvailableSkins());
        jGraphicPacks.setModel(model);
        String skin = GlobalOptions.getProperty("default.skin");
        if (skin != null) {
            if (model.getIndexOf(skin) != -1) {
                jGraphicPacks.setSelectedItem(skin);
            } else {
                jGraphicPacks.setSelectedItem("default");
            }
        } else {
            jGraphicPacks.setSelectedItem("default");
        }
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Network Setup">
        boolean useProxy = false;

        try {
            useProxy = Boolean.parseBoolean(GlobalOptions.getProperty("proxySet"));
        } catch (Exception e) {
            useProxy = false;
        }

        jDirectConnectOption.setSelected(!useProxy);
        jProxyConnectOption.setSelected(useProxy);

        if (GlobalOptions.getProperty("proxyHost") != null) {
            //System.setProperty("proxyHost", GlobalOptions.getProperty("proxyHost"));
            jProxyHost.setText(GlobalOptions.getProperty("proxyHost"));
        }
        if (GlobalOptions.getProperty("proxyPort") != null) {
            //System.setProperty("proxyPort", GlobalOptions.getProperty("proxyPort"));
            jProxyPort.setText(GlobalOptions.getProperty("proxyPort"));
        }
        if (GlobalOptions.getProperty("proxyType") != null) {
            // System.setProperty("proxyHost", GlobalOptions.getProperty("proxyHost"));
            try {
                jProxyTypeChooser.setSelectedIndex(Integer.parseInt(GlobalOptions.getProperty("proxyType")));
            } catch (Exception e) {
                jProxyTypeChooser.setSelectedIndex(0);
            }
        }

        if (GlobalOptions.getProperty("proxyUser") != null) {
            //System.setProperty("proxyPort", GlobalOptions.getProperty("proxyPort"));
            jProxyUser.setText(GlobalOptions.getProperty("proxyUser"));
        }

        if (GlobalOptions.getProperty("proxyPassword") != null) {
            //System.setProperty("proxyPort", GlobalOptions.getProperty("proxyPort"));
            jProxyPassword.setText(GlobalOptions.getProperty("proxyPassword"));
        }
        if (jProxyConnectOption.isSelected()) {
            SocketAddress addr = new InetSocketAddress(jProxyHost.getText(), Integer.parseInt(jProxyPort.getText()));
            switch (jProxyTypeChooser.getSelectedIndex()) {
                case 1: {
                    webProxy = new Proxy(Proxy.Type.SOCKS, addr);
                    break;
                }
                default: {
                    webProxy = new Proxy(Proxy.Type.HTTP, addr);
                    break;
                }
            }
            if ((jProxyUser.getText().length() >= 1) && (jProxyPassword.getPassword().length > 1)) {
                Authenticator.setDefault(new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(jProxyUser.getText(), jProxyPassword.getPassword());
                    }
                });
            }
        } else {
            webProxy = Proxy.NO_PROXY;
        }

        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Account Setup">
        String name = GlobalOptions.getProperty("account.name");
        String password = GlobalOptions.getProperty("account.password");

        if ((name != null) && (password != null)) {
            jAccountName.setText(name);
            jAccountPassword.setText(password);
        } else if (name != null) {
            jAccountName.setText(name);
        }
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Set properties ">

        //show popup moral
        try {
            String v = GlobalOptions.getProperty("show.popup.moral");
            if (v == null) {
                jShowPopupMoral.setSelected(true);
                GlobalOptions.addProperty("show.popup.moral", Boolean.toString(true));
            } else {
                if (Boolean.parseBoolean(v)) {
                    jShowPopupMoral.setSelected(true);
                }
            }
        } catch (Exception e) {
        }

        //show popup moral
        try {
            String v = GlobalOptions.getProperty("show.popup.conquers");
            if (v == null) {
                jShowPopupConquers.setSelected(true);
                GlobalOptions.addProperty("show.popup.conquers", Boolean.toString(true));
            } else {
                if (Boolean.parseBoolean(v)) {
                    jShowPopupConquers.setSelected(true);
                }
            }
        } catch (Exception e) {
        }

        //show popup ranks
        try {
            String v = GlobalOptions.getProperty("show.popup.ranks");
            if (v == null) {
                jShowPopupRanks.setSelected(true);
                GlobalOptions.addProperty("show.popup.ranks", Boolean.toString(true));
            } else {
                if (Boolean.parseBoolean(v)) {
                    jShowPopupRanks.setSelected(true);
                }
            }
        } catch (Exception e) {
        }

        //show continents
        try {
            if (Boolean.parseBoolean(GlobalOptions.getProperty("map.showcontinents"))) {
                jShowContinents.setSelected(true);
            }
        } catch (Exception e) {
        }
        //show sectors
        try {
            if (Boolean.parseBoolean(GlobalOptions.getProperty("show.sectors"))) {
                jShowSectorsBox.setSelected(true);
            }
        } catch (Exception e) {
        }

        //show barbarian
        try {
            String v = GlobalOptions.getProperty("show.barbarian");
            if (v == null) {
                jShowBarbarianBox.setSelected(true);
                GlobalOptions.addProperty("show.barbarian", Boolean.toString(true));
            } else if (Boolean.parseBoolean(v)) {
                jShowBarbarianBox.setSelected(true);
            }
        } catch (Exception e) {
        }

        //attack movement
        try {
            if (Boolean.parseBoolean(GlobalOptions.getProperty("attack.movement"))) {
                jShowAttackMovementBox.setSelected(true);
            }
        } catch (Exception e) {
        }

        //draw attacks by default
        try {
            if (Boolean.parseBoolean(GlobalOptions.getProperty("draw.attacks.by.default"))) {
                jDrawAttacksByDefaultBox.setSelected(true);
            }
        } catch (Exception e) {
        }

        //own villages on minmap
        try {
            if (Boolean.parseBoolean(GlobalOptions.getProperty("mark.villages.on.minimap"))) {
                jMarkOwnVillagesOnMinimapBox.setSelected(true);
            }
        } catch (Exception e) {
        }

        //mark active village
        try {
            if (Boolean.parseBoolean(GlobalOptions.getProperty("mark.active.village"))) {
                jMarkActiveVillageBox.setSelected(true);
            }
        } catch (Exception e) {
        }

        //default enemy marker color
        try {
            int sel = Integer.parseInt(GlobalOptions.getProperty("default.mark"));
            jDefaultMarkBox.setSelectedIndex(sel);
        } catch (Exception e) {
            jDefaultMarkBox.setSelectedIndex(0);
        }

        //village troop type
        try {
            if (Boolean.parseBoolean(GlobalOptions.getProperty("paint.troops.type"))) {
                jTroopsTypeBox.setSelected(true);
            }
        } catch (Exception e) {
        }

        //data update check on startup
        try {
            String value = GlobalOptions.getProperty("check.updates.on.startup");
            boolean check = false;
            if (value == null) {
                check = true;
            } else {
                check = Boolean.parseBoolean(value);
            }
            jCheckForUpdatesBox.setSelected(check);
        } catch (Exception e) {
        }

        //village sort order
        try {
            int villageOrder = Integer.parseInt(GlobalOptions.getProperty("village.order"));
            villageOrder = (villageOrder == 0 || villageOrder == 1) ? villageOrder : 0;
            Village.setOrderType(villageOrder);
            jVillageSortTypeChooser.setSelectedIndex(villageOrder);
        } catch (Exception e) {
        }

        //export tribes
        try {
            String prop = GlobalOptions.getProperty("export.tribe.names");
            if (prop == null) {
                //not set yet, so set true
                GlobalOptions.addProperty("export.tribe.names", "true");
                jExportTribeNames.setSelected(true);
            } else {
                if (Boolean.parseBoolean(prop)) {
                    jExportTribeNames.setSelected(true);
                }
            }
        } catch (Exception e) {
        }

        //export unit
        try {
            String prop = GlobalOptions.getProperty("export.units");
            if (prop == null) {
                //not set yet, so set true
                GlobalOptions.addProperty("export.units", "true");
                jExportUnit.setSelected(true);
            } else {
                if (Boolean.parseBoolean(prop)) {

                    jExportUnit.setSelected(true);
                }
            }
        } catch (Exception e) {
        }

        //export arrive time
        try {
            String prop = GlobalOptions.getProperty("export.arrive.time");
            if (prop == null) {
                //not set yet, so set true
                GlobalOptions.addProperty("export.arrive.time", "true");
                jExportArriveTime.setSelected(true);
            } else {
                if (Boolean.parseBoolean(prop)) {
                    jExportArriveTime.setSelected(true);
                }
            }
        } catch (Exception e) {
        }

        //notification visibility
        try {
            int notifyDuration = Integer.parseInt(GlobalOptions.getProperty("notify.duration"));
            jNotifyDurationBox.setSelectedIndex(notifyDuration);
        } catch (Exception e) {
        }

        //check for version updates
        try {
            String val = GlobalOptions.getProperty("inform.on.updates");
            if (val != null) {
                jInformOnUpdates.setSelected(Boolean.parseBoolean(val));
            }
        } catch (Exception e) {
        }

        //check for version updates
        try {
            String val = GlobalOptions.getProperty("max.density.troops");
            if (val != null) {
                jMaxTroopDensity.setText(val);
            }
        } catch (Exception e) {
            jMaxTroopDensity.setText("650000");
            GlobalOptions.addProperty("max.density.troops", "650000");
        }

        //overwrite markers on import
        try {
            String val = GlobalOptions.getProperty("import.replace.markers");
            if (val != null) {
                jReplaceMarkers.setSelected(Boolean.parseBoolean(val));
            }
        } catch (Exception e) {
        }

        //overwrite group markers on import
        try {
            String val = GlobalOptions.getProperty("import.replace.tag.markers");
            if (val != null) {
                jReplaceTagMarkers.setSelected(Boolean.parseBoolean(val));
            }
        } catch (Exception e) {
        }

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Init HelpSystem ">
        GlobalOptions.getHelpBroker().enableHelp(jLoginPanel, "pages.login_settings", GlobalOptions.getHelpBroker().getHelpSet());
        GlobalOptions.getHelpBroker().enableHelp(jPlayerServerSettings, "pages.player_server_settings", GlobalOptions.getHelpBroker().getHelpSet());
        GlobalOptions.getHelpBroker().enableHelp(jMapSettings, "pages.map_settings", GlobalOptions.getHelpBroker().getHelpSet());
        GlobalOptions.getHelpBroker().enableHelp(jAttackSettings, "pages.attack_settings", GlobalOptions.getHelpBroker().getHelpSet());
        GlobalOptions.getHelpBroker().enableHelp(jNetworkSettings, "pages.network_settings", GlobalOptions.getHelpBroker().getHelpSet());
        GlobalOptions.getHelpBroker().enableHelp(jMiscSettings, "pages.misc_settings", GlobalOptions.getHelpBroker().getHelpSet());
        GlobalOptions.getHelpBroker().enableHelpKey(getRootPane(), "pages.settings", GlobalOptions.getHelpBroker().getHelpSet());
    // </editor-fold>
    }

    public Proxy getWebProxy() {
        return webProxy;
    }

    protected void setupAttackColorTable() {
        jAttackColorTable.invalidate();
        DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Einheit", "Farbe"
                }) {

            Class[] types = new Class[]{
                String.class, Color.class
            };
            boolean[] canEdit = new boolean[]{
                false, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jAttackColorTable.setDefaultRenderer(Color.class, new ColorCellRenderer());
        jAttackColorTable.setDefaultEditor(Color.class, new ColorChooserCellEditor(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //not needed
            }
        }));

        jAttackColorTable.setModel(model);
        jAttackColorTable.getColumnModel().getColumn(1).setMaxWidth(75);
        for (UnitHolder unit : DataHolder.getSingleton().getUnits()) {
            String hexColor = GlobalOptions.getProperty(unit.getName() + ".color");
            if (hexColor == null) {
                //no color set yet, take red
                hexColor = Integer.toHexString(Color.RED.getRGB());
                hexColor = "#" + hexColor.substring(2, hexColor.length());
                GlobalOptions.addProperty(unit.getName() + ".color", hexColor);
                model.addRow(new Object[]{unit, Color.RED});
            } else {
                try {
                    Color col = Color.decode(hexColor);
                    model.addRow(new Object[]{unit, col});
                } catch (Exception e) {
                    logger.warn("Failed to decode color " + hexColor + ". Switch to default");
                    hexColor = Integer.toHexString(Color.RED.getRGB());
                    hexColor = "#" + hexColor.substring(2, hexColor.length());
                    GlobalOptions.addProperty(unit.getName() + ".color", hexColor);
                    model.addRow(new Object[]{unit, Color.RED});
                }
            }
        }

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, hasFocus, hasFocus, row, row);
                DefaultTableCellRenderer r = ((DefaultTableCellRenderer) c);
                r.setText("<html><b>" + r.getText() + "</b></html>");
                c.setBackground(Constants.DS_BACK);
                return c;
            }
        };

        for (int i = 0; i < jAttackColorTable.getColumnCount(); i++) {
            jAttackColorTable.getColumn(jAttackColorTable.getColumnName(i)).setHeaderRenderer(headerRenderer);
        }
        jAttackColorTable.revalidate();
    }

    @Override
    public void setVisible(boolean pValue) {
        if (!INITIALIZED) {
            if (!DataHolder.getSingleton().getUnits().isEmpty()) {
                setupAttackColorTable();
                INITIALIZED = true;
            } else {
                //units not loaded yet
            }
        }
        super.setVisible(pValue);
    }

    protected boolean checkSettings() {
        logger.debug("Checking settings");
        checkConnectivity();
        if (!updateServerList()) {
            //remote update failed and no local servers found
            String message = "Serverliste konnte nicht geladen werden.\n" +
                    "Mögliche Ursachen sind fehlerhafte Netzwerkeinstellungen, keine Verbindung zum Internet\n" +
                    "oder 'dsworkbench.de' ist nicht verfügbar.\n" +
                    "Da noch kein Datenabgleich mit dem Server stattgefunden hat\n" +
                    "korrigiere bitte deine Netzwerkeinstellungen um diesen einmalig durchzuführen.";
            JOptionPaneHelper.showWarningBox(this, message, "Warnung");
            return false;
        }

        if (!checkAccountSettings()) {
            return false;
        }

        return checkTribesAccountSettings();
    }

    //check server and player settings
    private boolean checkServerPlayerSettings() {
        String defaultServer = GlobalOptions.getProperty("default.server");

        //check if default server exists
        if (defaultServer == null) {
            //try setting current server to default
            logger.warn("Default server is not set");
            String selection = (String) jServerList.getSelectedItem();
            if ((selection != null) && (selection.length() > 1)) {
                //set current server to default
                GlobalOptions.setSelectedServer(selection);
                GlobalOptions.addProperty("default.server", (String) selection);
                defaultServer = selection;
            } else {
                //no server selected
                return false;
            }
        }

        String serverUser = GlobalOptions.getProperty("player." + defaultServer);
        if (serverUser == null) {
            logger.warn("Default user for server '" + defaultServer + "' is not set");
            String selection = (String) jTribeNames.getSelectedItem();
            //check if selection is valid
            if ((selection != null) && (!selection.equals("Bitte wählen")) && (selection.length() > 1)) {
                //set default user for server
                GlobalOptions.addProperty("player." + defaultServer, selection);
            } else {
                //no default user selected
                return false;
            }
        }
        return true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        connectionTypeGroup = new javax.swing.ButtonGroup();
        jCreateAccountDialog = new javax.swing.JDialog();
        jRegisterNameLabel = new javax.swing.JLabel();
        jRegistrationAccountName = new javax.swing.JTextField();
        jRegisterPasswordLabel = new javax.swing.JLabel();
        jRegistrationPassword = new javax.swing.JPasswordField();
        jRegisterButton = new javax.swing.JButton();
        jCancelRegistrationButton = new javax.swing.JButton();
        jRepeatPasswordLabel = new javax.swing.JLabel();
        jRegistrationPassword2 = new javax.swing.JPasswordField();
        tagMarkerGroup = new javax.swing.ButtonGroup();
        jChangePasswordDialog = new javax.swing.JDialog();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jNewPassword2 = new javax.swing.JPasswordField();
        jNewPassword = new javax.swing.JPasswordField();
        jOldPassword = new javax.swing.JPasswordField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jPasswordChangeAccount = new javax.swing.JTextField();
        jTroopDensitySelectionDialog = new javax.swing.JDialog();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jSpearAmount = new javax.swing.JTextField();
        jSwordAmount = new javax.swing.JTextField();
        jArcherAmount = new javax.swing.JTextField();
        jHeavyAmount = new javax.swing.JTextField();
        jDeffStrengthOKButton = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jSettingsTabbedPane = new javax.swing.JTabbedPane();
        jLoginPanel = new javax.swing.JPanel();
        jAccountNameLabel = new javax.swing.JLabel();
        jAccountPasswordLabel = new javax.swing.JLabel();
        jAccountPassword = new javax.swing.JPasswordField();
        jAccountName = new javax.swing.JTextField();
        jCheckAccountButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPlayerServerSettings = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jServerList = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTribeNames = new javax.swing.JComboBox();
        jSelectServerButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jStatusArea = new javax.swing.JTextArea();
        jDownloadDataButton = new javax.swing.JButton();
        jCheckForUpdatesBox = new javax.swing.JCheckBox();
        jMapSettings = new javax.swing.JPanel();
        jSkinPackLabel = new javax.swing.JLabel();
        jGraphicPacks = new javax.swing.JComboBox();
        jSelectSkinButton = new javax.swing.JButton();
        jPreviewSkinButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jShowContinents = new javax.swing.JCheckBox();
        jShowSectorsBox = new javax.swing.JCheckBox();
        jMarkOwnVillagesOnMinimapBox = new javax.swing.JCheckBox();
        jMarkActiveVillageBox = new javax.swing.JCheckBox();
        jTroopsTypeBox = new javax.swing.JCheckBox();
        jShowBarbarianBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jShowContinentsLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jAttackMovementLabel2 = new javax.swing.JLabel();
        jAttackMovementLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDefaultMarkBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jShowPopupMoral = new javax.swing.JCheckBox();
        jShowPopupConquers = new javax.swing.JCheckBox();
        jShowPopupRanks = new javax.swing.JCheckBox();
        jAttackSettings = new javax.swing.JPanel();
        jAttackMovementLabel = new javax.swing.JLabel();
        jShowAttackMovementBox = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jAttackColorTable = new javax.swing.JTable();
        jAttackMovementLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jDrawAttacksByDefaultBox = new javax.swing.JCheckBox();
        jNetworkSettings = new javax.swing.JPanel();
        jDirectConnectOption = new javax.swing.JRadioButton();
        jProxyConnectOption = new javax.swing.JRadioButton();
        jProxyAdressLabel = new javax.swing.JLabel();
        jProxyHost = new javax.swing.JTextField();
        jProxyPortLabel = new javax.swing.JLabel();
        jProxyPort = new javax.swing.JTextField();
        jRefeshNetworkButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jProxyTypeChooser = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jProxyUser = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jProxyPassword = new javax.swing.JPasswordField();
        jMiscSettings = new javax.swing.JPanel();
        jVillageSortTypeChooser = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jNotifyDurationBox = new javax.swing.JComboBox();
        jInformOnUpdates = new javax.swing.JCheckBox();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jExportTribeNames = new javax.swing.JCheckBox();
        jExportArriveTime = new javax.swing.JCheckBox();
        jExportUnit = new javax.swing.JCheckBox();
        jLabel34 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jReplaceMarkers = new javax.swing.JCheckBox();
        jReplaceTagMarkers = new javax.swing.JCheckBox();
        jLabel39 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jMaxTroopDensity = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jOKButton = new javax.swing.JButton();
        jCancelButton = new javax.swing.JButton();
        jCreateAccountButton = new javax.swing.JButton();
        jChangePasswordButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/tor/tribes/ui/Bundle"); // NOI18N
        jCreateAccountDialog.setTitle(bundle.getString("DSWorkbenchSettingsDialog.jCreateAccountDialog.title")); // NOI18N
        jCreateAccountDialog.setAlwaysOnTop(true);
        jCreateAccountDialog.setBackground(new java.awt.Color(239, 235, 223));
        jCreateAccountDialog.setModal(true);

        jRegisterNameLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jRegisterNameLabel.text")); // NOI18N

        jRegistrationAccountName.setMaximumSize(new java.awt.Dimension(200, 20));
        jRegistrationAccountName.setMinimumSize(new java.awt.Dimension(200, 20));
        jRegistrationAccountName.setPreferredSize(new java.awt.Dimension(200, 20));

        jRegisterPasswordLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jRegisterPasswordLabel.text")); // NOI18N

        jRegistrationPassword.setMaximumSize(new java.awt.Dimension(200, 20));
        jRegistrationPassword.setMinimumSize(new java.awt.Dimension(200, 20));
        jRegistrationPassword.setPreferredSize(new java.awt.Dimension(200, 20));

        jRegisterButton.setBackground(new java.awt.Color(239, 235, 223));
        jRegisterButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jRegisterButton.text")); // NOI18N
        jRegisterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireRegisterEvent(evt);
            }
        });

        jCancelRegistrationButton.setBackground(new java.awt.Color(239, 235, 223));
        jCancelRegistrationButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jCancelRegistrationButton.text")); // NOI18N
        jCancelRegistrationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireCancelRegistrationEvent(evt);
            }
        });

        jRepeatPasswordLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jRepeatPasswordLabel.text")); // NOI18N

        javax.swing.GroupLayout jCreateAccountDialogLayout = new javax.swing.GroupLayout(jCreateAccountDialog.getContentPane());
        jCreateAccountDialog.getContentPane().setLayout(jCreateAccountDialogLayout);
        jCreateAccountDialogLayout.setHorizontalGroup(
            jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCreateAccountDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRegisterPasswordLabel)
                    .addComponent(jRegisterNameLabel)
                    .addComponent(jRepeatPasswordLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRegistrationPassword2, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addGroup(jCreateAccountDialogLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCancelRegistrationButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRegisterButton, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                    .addComponent(jRegistrationAccountName, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(jRegistrationPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                .addContainerGap())
        );
        jCreateAccountDialogLayout.setVerticalGroup(
            jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCreateAccountDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRegisterNameLabel)
                    .addComponent(jRegistrationAccountName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRegisterPasswordLabel)
                    .addComponent(jRegistrationPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRepeatPasswordLabel)
                    .addComponent(jRegistrationPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCreateAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCancelRegistrationButton)
                    .addComponent(jRegisterButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jChangePasswordDialog.setTitle(bundle.getString("DSWorkbenchSettingsDialog.jChangePasswordDialog.title")); // NOI18N
        jChangePasswordDialog.setAlwaysOnTop(true);
        jChangePasswordDialog.setModal(true);

        jLabel35.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel35.text")); // NOI18N

        jLabel36.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel36.text")); // NOI18N

        jLabel37.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel37.text")); // NOI18N

        jNewPassword2.setText(bundle.getString("DSWorkbenchSettingsDialog.jNewPassword2.text")); // NOI18N

        jNewPassword.setText(bundle.getString("DSWorkbenchSettingsDialog.jNewPassword.text")); // NOI18N

        jOldPassword.setText(bundle.getString("DSWorkbenchSettingsDialog.jOldPassword.text")); // NOI18N

        jButton9.setText(bundle.getString("DSWorkbenchSettingsDialog.jButton9.text")); // NOI18N
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireDoChangePasswordEvent(evt);
            }
        });

        jButton10.setText(bundle.getString("DSWorkbenchSettingsDialog.jButton10.text")); // NOI18N
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireCancelChangePasswordEvent(evt);
            }
        });

        jLabel38.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel38.text")); // NOI18N

        jPasswordChangeAccount.setEditable(false);
        jPasswordChangeAccount.setText(bundle.getString("DSWorkbenchSettingsDialog.jPasswordChangeAccount.text")); // NOI18N

        javax.swing.GroupLayout jChangePasswordDialogLayout = new javax.swing.GroupLayout(jChangePasswordDialog.getContentPane());
        jChangePasswordDialog.getContentPane().setLayout(jChangePasswordDialogLayout);
        jChangePasswordDialogLayout.setHorizontalGroup(
            jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jChangePasswordDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jLabel36)
                    .addComponent(jLabel35)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jChangePasswordDialogLayout.createSequentialGroup()
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9))
                    .addComponent(jOldPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(jNewPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(jNewPassword2, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(jPasswordChangeAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addContainerGap())
        );
        jChangePasswordDialogLayout.setVerticalGroup(
            jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jChangePasswordDialogLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jPasswordChangeAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jOldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jNewPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jChangePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton9))
                .addContainerGap())
        );

        jTroopDensitySelectionDialog.setTitle(bundle.getString("DSWorkbenchSettingsDialog.jTroopDensitySelectionDialog.title")); // NOI18N
        jTroopDensitySelectionDialog.setAlwaysOnTop(true);
        jTroopDensitySelectionDialog.setModal(true);

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/spear.png"))); // NOI18N
        jLabel40.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel40.text")); // NOI18N

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/sword.png"))); // NOI18N
        jLabel41.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel41.text")); // NOI18N

        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/archer.png"))); // NOI18N
        jLabel42.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel42.text")); // NOI18N

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/heavy.png"))); // NOI18N
        jLabel43.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel43.text")); // NOI18N

        jSpearAmount.setText(bundle.getString("DSWorkbenchSettingsDialog.jSpearAmount.text")); // NOI18N
        jSpearAmount.setMaximumSize(new java.awt.Dimension(50, 20));
        jSpearAmount.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpearAmount.setPreferredSize(new java.awt.Dimension(50, 20));

        jSwordAmount.setText(bundle.getString("DSWorkbenchSettingsDialog.jSwordAmount.text")); // NOI18N
        jSwordAmount.setMaximumSize(new java.awt.Dimension(50, 20));
        jSwordAmount.setMinimumSize(new java.awt.Dimension(50, 20));
        jSwordAmount.setPreferredSize(new java.awt.Dimension(50, 20));

        jArcherAmount.setText(bundle.getString("DSWorkbenchSettingsDialog.jArcherAmount.text")); // NOI18N
        jArcherAmount.setMaximumSize(new java.awt.Dimension(50, 20));
        jArcherAmount.setMinimumSize(new java.awt.Dimension(50, 20));
        jArcherAmount.setPreferredSize(new java.awt.Dimension(50, 20));

        jHeavyAmount.setText(bundle.getString("DSWorkbenchSettingsDialog.jHeavyAmount.text")); // NOI18N
        jHeavyAmount.setMaximumSize(new java.awt.Dimension(50, 20));
        jHeavyAmount.setMinimumSize(new java.awt.Dimension(50, 20));
        jHeavyAmount.setPreferredSize(new java.awt.Dimension(50, 20));

        jDeffStrengthOKButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jDeffStrengthOKButton.text")); // NOI18N
        jDeffStrengthOKButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireAcceptDeffStrengthEvent(evt);
            }
        });

        jButton12.setText(bundle.getString("DSWorkbenchSettingsDialog.jButton12.text")); // NOI18N
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireAcceptDeffStrengthEvent(evt);
            }
        });

        javax.swing.GroupLayout jTroopDensitySelectionDialogLayout = new javax.swing.GroupLayout(jTroopDensitySelectionDialog.getContentPane());
        jTroopDensitySelectionDialog.getContentPane().setLayout(jTroopDensitySelectionDialogLayout);
        jTroopDensitySelectionDialogLayout.setHorizontalGroup(
            jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                        .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addGap(10, 10, 10)
                                .addComponent(jSpearAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                            .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addGap(10, 10, 10)
                                .addComponent(jSwordAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                                .addComponent(jLabel43)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jHeavyAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jArcherAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jTroopDensitySelectionDialogLayout.createSequentialGroup()
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDeffStrengthOKButton)))
                .addContainerGap())
        );
        jTroopDensitySelectionDialogLayout.setVerticalGroup(
            jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jTroopDensitySelectionDialogLayout.createSequentialGroup()
                            .addComponent(jArcherAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jHeavyAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jTroopDensitySelectionDialogLayout.createSequentialGroup()
                            .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel42)
                                .addComponent(jSpearAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSwordAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel43))))
                    .addGroup(jTroopDensitySelectionDialogLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel41)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jTroopDensitySelectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDeffStrengthOKButton)
                    .addComponent(jButton12))
                .addContainerGap())
        );

        setTitle(bundle.getString("DSWorkbenchSettingsDialog.title")); // NOI18N
        setAlwaysOnTop(true);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                fireClosingEvent(evt);
            }
        });

        jSettingsTabbedPane.setBackground(new java.awt.Color(239, 235, 223));

        jLoginPanel.setBackground(new java.awt.Color(239, 235, 223));

        jAccountNameLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jAccountNameLabel.text")); // NOI18N

        jAccountPasswordLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jAccountPasswordLabel.text")); // NOI18N

        jAccountPassword.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jAccountPassword.toolTipText")); // NOI18N
        jAccountPassword.setMaximumSize(new java.awt.Dimension(200, 20));
        jAccountPassword.setMinimumSize(new java.awt.Dimension(200, 20));
        jAccountPassword.setPreferredSize(new java.awt.Dimension(200, 20));

        jAccountName.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jAccountName.toolTipText")); // NOI18N
        jAccountName.setMaximumSize(new java.awt.Dimension(200, 20));
        jAccountName.setMinimumSize(new java.awt.Dimension(200, 20));
        jAccountName.setPreferredSize(new java.awt.Dimension(200, 20));

        jCheckAccountButton.setBackground(new java.awt.Color(239, 235, 223));
        jCheckAccountButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jCheckAccountButton.text")); // NOI18N
        jCheckAccountButton.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jCheckAccountButton.toolTipText")); // NOI18N
        jCheckAccountButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireLoginIntoAccountEvent(evt);
            }
        });

        jEditorPane1.setContentType(bundle.getString("DSWorkbenchSettingsDialog.jEditorPane1.contentType")); // NOI18N
        jEditorPane1.setEditable(false);
        jEditorPane1.setText(bundle.getString("DSWorkbenchSettingsDialog.jEditorPane1.text")); // NOI18N
        jEditorPane1.setOpaque(false);
        jScrollPane3.setViewportView(jEditorPane1);

        javax.swing.GroupLayout jLoginPanelLayout = new javax.swing.GroupLayout(jLoginPanel);
        jLoginPanel.setLayout(jLoginPanelLayout);
        jLoginPanelLayout.setHorizontalGroup(
            jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                    .addGroup(jLoginPanelLayout.createSequentialGroup()
                        .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jAccountNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jAccountPasswordLabel))
                        .addGap(21, 21, 21)
                        .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCheckAccountButton)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jAccountPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jAccountName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jLoginPanelLayout.setVerticalGroup(
            jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jAccountName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jAccountNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jAccountPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jAccountPasswordLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckAccountButton)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        jSettingsTabbedPane.addTab(bundle.getString("DSWorkbenchSettingsDialog.jLoginPanel.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/res/login.png")), jLoginPanel); // NOI18N

        jPlayerServerSettings.setBackground(new java.awt.Color(239, 235, 223));

        jLabel1.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel1.text")); // NOI18N

        jServerList.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jServerList.toolTipText")); // NOI18N

        jLabel2.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel2.text")); // NOI18N

        jTribeNames.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jTribeNames.toolTipText")); // NOI18N

        jSelectServerButton.setBackground(new java.awt.Color(239, 235, 223));
        jSelectServerButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jSelectServerButton.text")); // NOI18N
        jSelectServerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireSelectServerEvent(evt);
            }
        });

        jStatusArea.setColumns(20);
        jStatusArea.setEditable(false);
        jStatusArea.setRows(5);
        jScrollPane1.setViewportView(jStatusArea);

        jDownloadDataButton.setBackground(new java.awt.Color(239, 235, 223));
        jDownloadDataButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jDownloadDataButton.text")); // NOI18N
        jDownloadDataButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireDownloadDataEvent(evt);
            }
        });

        jCheckForUpdatesBox.setText(bundle.getString("DSWorkbenchSettingsDialog.jCheckForUpdatesBox.text")); // NOI18N
        jCheckForUpdatesBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jCheckForUpdatesBox.toolTipText")); // NOI18N
        jCheckForUpdatesBox.setOpaque(false);
        jCheckForUpdatesBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fireCheckForUpdatesEvent(evt);
            }
        });

        javax.swing.GroupLayout jPlayerServerSettingsLayout = new javax.swing.GroupLayout(jPlayerServerSettings);
        jPlayerServerSettings.setLayout(jPlayerServerSettingsLayout);
        jPlayerServerSettingsLayout.setHorizontalGroup(
            jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPlayerServerSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckForUpdatesBox)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                    .addGroup(jPlayerServerSettingsLayout.createSequentialGroup()
                        .addGroup(jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTribeNames, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jServerList, 0, 262, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDownloadDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(jSelectServerButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPlayerServerSettingsLayout.setVerticalGroup(
            jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPlayerServerSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jServerList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSelectServerButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPlayerServerSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTribeNames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDownloadDataButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckForUpdatesBox)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jSettingsTabbedPane.addTab(bundle.getString("DSWorkbenchSettingsDialog.jPlayerServerSettings.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/res/face.png")), jPlayerServerSettings); // NOI18N

        jMapSettings.setBackground(new java.awt.Color(239, 235, 223));

        jSkinPackLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jSkinPackLabel.text")); // NOI18N

        jGraphicPacks.setMaximumSize(new java.awt.Dimension(114, 22));
        jGraphicPacks.setMinimumSize(new java.awt.Dimension(114, 22));
        jGraphicPacks.setPreferredSize(new java.awt.Dimension(114, 22));

        jSelectSkinButton.setBackground(new java.awt.Color(239, 235, 223));
        jSelectSkinButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jSelectSkinButton.text")); // NOI18N
        jSelectSkinButton.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jSelectSkinButton.toolTipText")); // NOI18N
        jSelectSkinButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireSelectGraphicPackEvent(evt);
            }
        });

        jPreviewSkinButton.setBackground(new java.awt.Color(239, 235, 223));
        jPreviewSkinButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jPreviewSkinButton.text")); // NOI18N
        jPreviewSkinButton.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jPreviewSkinButton.toolTipText")); // NOI18N
        jPreviewSkinButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireShowSkinPreviewEvent(evt);
            }
        });

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jShowContinents.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jShowContinents.toolTipText")); // NOI18N
        jShowContinents.setContentAreaFilled(false);
        jShowContinents.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fireChangeContinentsOnMinimapEvent(evt);
            }
        });
        jPanel1.add(jShowContinents);

        jShowSectorsBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jShowSectorsBox.toolTipText")); // NOI18N
        jShowSectorsBox.setOpaque(false);
        jShowSectorsBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fireChangeShowSectorsEvent(evt);
            }
        });
        jPanel1.add(jShowSectorsBox);

        jMarkOwnVillagesOnMinimapBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jMarkOwnVillagesOnMinimapBox.toolTipText")); // NOI18N
        jMarkOwnVillagesOnMinimapBox.setOpaque(false);
        jMarkOwnVillagesOnMinimapBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireChangeMarkOwnVillagesOnMinimapEvent(evt);
            }
        });
        jPanel1.add(jMarkOwnVillagesOnMinimapBox);

        jMarkActiveVillageBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jMarkActiveVillageBox.toolTipText")); // NOI18N
        jMarkActiveVillageBox.setOpaque(false);
        jMarkActiveVillageBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireChangeMarkActiveVillageEvent(evt);
            }
        });
        jPanel1.add(jMarkActiveVillageBox);

        jTroopsTypeBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jTroopsTypeBox.toolTipText")); // NOI18N
        jTroopsTypeBox.setOpaque(false);
        jTroopsTypeBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireShowTroopsTypeEvent(evt);
            }
        });
        jPanel1.add(jTroopsTypeBox);

        jShowBarbarianBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jShowBarbarianBox.toolTipText")); // NOI18N
        jShowBarbarianBox.setOpaque(false);
        jShowBarbarianBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireShowBarbarianChangedEvent(evt);
            }
        });
        jPanel1.add(jShowBarbarianBox);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jShowContinentsLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jShowContinentsLabel.text")); // NOI18N
        jShowContinentsLabel.setMaximumSize(new java.awt.Dimension(150, 21));
        jShowContinentsLabel.setMinimumSize(new java.awt.Dimension(150, 21));
        jShowContinentsLabel.setPreferredSize(new java.awt.Dimension(150, 21));
        jPanel2.add(jShowContinentsLabel);

        jLabel7.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel7.text")); // NOI18N
        jLabel7.setMaximumSize(new java.awt.Dimension(150, 21));
        jLabel7.setMinimumSize(new java.awt.Dimension(150, 21));
        jLabel7.setPreferredSize(new java.awt.Dimension(150, 21));
        jPanel2.add(jLabel7);

        jAttackMovementLabel2.setText(bundle.getString("DSWorkbenchSettingsDialog.jAttackMovementLabel2.text")); // NOI18N
        jAttackMovementLabel2.setMaximumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel2.setMinimumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel2.setPreferredSize(new java.awt.Dimension(150, 21));
        jPanel2.add(jAttackMovementLabel2);

        jAttackMovementLabel1.setText(bundle.getString("DSWorkbenchSettingsDialog.jAttackMovementLabel1.text")); // NOI18N
        jAttackMovementLabel1.setMaximumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel1.setMinimumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel1.setPreferredSize(new java.awt.Dimension(150, 21));
        jPanel2.add(jAttackMovementLabel1);

        jLabel8.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel8.text")); // NOI18N
        jLabel8.setMaximumSize(new java.awt.Dimension(150, 21));
        jLabel8.setMinimumSize(new java.awt.Dimension(150, 21));
        jLabel8.setPreferredSize(new java.awt.Dimension(150, 21));
        jPanel2.add(jLabel8);

        jLabel17.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel17.text")); // NOI18N
        jLabel17.setMaximumSize(new java.awt.Dimension(150, 21));
        jLabel17.setMinimumSize(new java.awt.Dimension(150, 21));
        jLabel17.setPreferredSize(new java.awt.Dimension(150, 21));
        jPanel2.add(jLabel17);

        jLabel4.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel4.text")); // NOI18N

        jDefaultMarkBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Weiß", "Rot" }));
        jDefaultMarkBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jDefaultMarkBox.toolTipText")); // NOI18N
        jDefaultMarkBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fireStandardMarkChangedEvent(evt);
            }
        });

        jLabel3.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel3.text")); // NOI18N

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        jShowPopupMoral.setText(bundle.getString("DSWorkbenchSettingsDialog.jShowPopupMoral.text")); // NOI18N
        jShowPopupMoral.setOpaque(false);
        jPanel5.add(jShowPopupMoral);

        jShowPopupConquers.setText(bundle.getString("DSWorkbenchSettingsDialog.jShowPopupConquers.text")); // NOI18N
        jShowPopupConquers.setOpaque(false);
        jPanel5.add(jShowPopupConquers);

        jShowPopupRanks.setText(bundle.getString("DSWorkbenchSettingsDialog.jShowPopupRanks.text")); // NOI18N
        jShowPopupRanks.setOpaque(false);
        jPanel5.add(jShowPopupRanks);

        javax.swing.GroupLayout jMapSettingsLayout = new javax.swing.GroupLayout(jMapSettings);
        jMapSettings.setLayout(jMapSettingsLayout);
        jMapSettingsLayout.setHorizontalGroup(
            jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMapSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSkinPackLabel)
                    .addComponent(jLabel4)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(36, 36, 36)
                .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jMapSettingsLayout.createSequentialGroup()
                        .addComponent(jDefaultMarkBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMapSettingsLayout.createSequentialGroup()
                        .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jGraphicPacks, 0, 180, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPreviewSkinButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSelectSkinButton, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(41, 41, 41))))
        );
        jMapSettingsLayout.setVerticalGroup(
            jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMapSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSelectSkinButton)
                    .addComponent(jSkinPackLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jGraphicPacks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPreviewSkinButton)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jDefaultMarkBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jMapSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        jSettingsTabbedPane.addTab(bundle.getString("DSWorkbenchSettingsDialog.jMapSettings.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/res/ui/map.gif")), jMapSettings); // NOI18N

        jAttackSettings.setBackground(new java.awt.Color(239, 235, 223));

        jAttackMovementLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jAttackMovementLabel.text")); // NOI18N
        jAttackMovementLabel.setMaximumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel.setMinimumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel.setPreferredSize(new java.awt.Dimension(150, 21));

        jShowAttackMovementBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jShowAttackMovementBox.toolTipText")); // NOI18N
        jShowAttackMovementBox.setOpaque(false);
        jShowAttackMovementBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireChangeShowAttackMovementEvent(evt);
            }
        });

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setOpaque(false);

        jAttackColorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jAttackColorTable.setOpaque(false);
        jScrollPane2.setViewportView(jAttackColorTable);

        jAttackMovementLabel3.setText(bundle.getString("DSWorkbenchSettingsDialog.jAttackMovementLabel3.text")); // NOI18N
        jAttackMovementLabel3.setMaximumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel3.setMinimumSize(new java.awt.Dimension(150, 21));
        jAttackMovementLabel3.setPreferredSize(new java.awt.Dimension(150, 21));

        jLabel9.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel9.text")); // NOI18N

        jDrawAttacksByDefaultBox.setText(bundle.getString("DSWorkbenchSettingsDialog.jDrawAttacksByDefaultBox.text")); // NOI18N
        jDrawAttacksByDefaultBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jDrawAttacksByDefaultBox.toolTipText")); // NOI18N
        jDrawAttacksByDefaultBox.setOpaque(false);
        jDrawAttacksByDefaultBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireDrawAttacksByDefaultChangedEvent(evt);
            }
        });

        javax.swing.GroupLayout jAttackSettingsLayout = new javax.swing.GroupLayout(jAttackSettings);
        jAttackSettings.setLayout(jAttackSettingsLayout);
        jAttackSettingsLayout.setHorizontalGroup(
            jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAttackSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jAttackMovementLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jAttackMovementLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jAttackSettingsLayout.createSequentialGroup()
                        .addGroup(jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jShowAttackMovementBox, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE))
                        .addGap(13, 13, 13))
                    .addGroup(jAttackSettingsLayout.createSequentialGroup()
                        .addComponent(jDrawAttacksByDefaultBox, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jAttackSettingsLayout.setVerticalGroup(
            jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAttackSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jAttackMovementLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jShowAttackMovementBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDrawAttacksByDefaultBox)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jAttackSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jAttackMovementLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSettingsTabbedPane.addTab(bundle.getString("DSWorkbenchSettingsDialog.jAttackSettings.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/res/barracks.png")), jAttackSettings); // NOI18N

        jNetworkSettings.setBackground(new java.awt.Color(239, 235, 223));

        connectionTypeGroup.add(jDirectConnectOption);
        jDirectConnectOption.setSelected(true);
        jDirectConnectOption.setText(bundle.getString("DSWorkbenchSettingsDialog.jDirectConnectOption.text")); // NOI18N
        jDirectConnectOption.setOpaque(false);
        jDirectConnectOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fireChangeConnectTypeEvent(evt);
            }
        });

        connectionTypeGroup.add(jProxyConnectOption);
        jProxyConnectOption.setText(bundle.getString("DSWorkbenchSettingsDialog.jProxyConnectOption.text")); // NOI18N
        jProxyConnectOption.setOpaque(false);
        jProxyConnectOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fireChangeConnectTypeEvent(evt);
            }
        });

        jProxyAdressLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jProxyAdressLabel.text")); // NOI18N

        jProxyHost.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jProxyHost.toolTipText")); // NOI18N
        jProxyHost.setEnabled(false);

        jProxyPortLabel.setText(bundle.getString("DSWorkbenchSettingsDialog.jProxyPortLabel.text")); // NOI18N

        jProxyPort.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jProxyPort.toolTipText")); // NOI18N
        jProxyPort.setEnabled(false);
        jProxyPort.setMaximumSize(new java.awt.Dimension(40, 20));
        jProxyPort.setMinimumSize(new java.awt.Dimension(40, 20));
        jProxyPort.setPreferredSize(new java.awt.Dimension(40, 20));

        jRefeshNetworkButton.setBackground(new java.awt.Color(239, 235, 223));
        jRefeshNetworkButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jRefeshNetworkButton.text")); // NOI18N
        jRefeshNetworkButton.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jRefeshNetworkButton.toolTipText")); // NOI18N
        jRefeshNetworkButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireUpdateProxySettingsEvent(evt);
            }
        });

        jLabel10.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel10.text")); // NOI18N

        jProxyTypeChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HTTP", "SOCKS" }));
        jProxyTypeChooser.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jProxyTypeChooser.toolTipText")); // NOI18N
        jProxyTypeChooser.setEnabled(false);

        jLabel11.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel11.text")); // NOI18N

        jProxyUser.setText(bundle.getString("DSWorkbenchSettingsDialog.jProxyUser.text")); // NOI18N
        jProxyUser.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jProxyUser.toolTipText")); // NOI18N
        jProxyUser.setEnabled(false);
        jProxyUser.setMaximumSize(new java.awt.Dimension(150, 20));
        jProxyUser.setMinimumSize(new java.awt.Dimension(150, 20));
        jProxyUser.setPreferredSize(new java.awt.Dimension(150, 20));

        jLabel12.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel12.text")); // NOI18N

        jProxyPassword.setText(bundle.getString("DSWorkbenchSettingsDialog.jProxyPassword.text")); // NOI18N
        jProxyPassword.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jProxyPassword.toolTipText")); // NOI18N
        jProxyPassword.setEnabled(false);
        jProxyPassword.setMaximumSize(new java.awt.Dimension(150, 20));
        jProxyPassword.setMinimumSize(new java.awt.Dimension(150, 20));
        jProxyPassword.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout jNetworkSettingsLayout = new javax.swing.GroupLayout(jNetworkSettings);
        jNetworkSettings.setLayout(jNetworkSettingsLayout);
        jNetworkSettingsLayout.setHorizontalGroup(
            jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jNetworkSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDirectConnectOption)
                    .addComponent(jProxyConnectOption)
                    .addGroup(jNetworkSettingsLayout.createSequentialGroup()
                        .addGroup(jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProxyAdressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jProxyPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProxyUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProxyTypeChooser, 0, 150, Short.MAX_VALUE)
                            .addComponent(jProxyHost, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(jRefeshNetworkButton, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addComponent(jProxyPortLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jProxyPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(156, 156, 156)))
                .addGap(126, 126, 126))
        );
        jNetworkSettingsLayout.setVerticalGroup(
            jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jNetworkSettingsLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jDirectConnectOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProxyConnectOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jProxyAdressLabel)
                    .addComponent(jProxyHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jProxyPortLabel)
                    .addComponent(jProxyPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jProxyTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jProxyUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jNetworkSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jProxyPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addComponent(jRefeshNetworkButton)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        jSettingsTabbedPane.addTab(bundle.getString("DSWorkbenchSettingsDialog.jNetworkSettings.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/res/proxy.png")), jNetworkSettings); // NOI18N

        jMiscSettings.setBackground(new java.awt.Color(239, 235, 223));

        jVillageSortTypeChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alphabetisch", "Nach Koordinaten" }));
        jVillageSortTypeChooser.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jVillageSortTypeChooser.toolTipText")); // NOI18N
        jVillageSortTypeChooser.setMaximumSize(new java.awt.Dimension(105, 18));
        jVillageSortTypeChooser.setPreferredSize(new java.awt.Dimension(105, 18));

        jLabel13.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel13.text")); // NOI18N
        jLabel13.setMaximumSize(new java.awt.Dimension(138, 18));
        jLabel13.setMinimumSize(new java.awt.Dimension(138, 18));
        jLabel13.setPreferredSize(new java.awt.Dimension(138, 18));

        jLabel14.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel14.text")); // NOI18N
        jLabel14.setMaximumSize(new java.awt.Dimension(138, 18));
        jLabel14.setMinimumSize(new java.awt.Dimension(138, 18));
        jLabel14.setPreferredSize(new java.awt.Dimension(138, 18));

        jNotifyDurationBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Unbegrenzt", "10 Sekunden", "20 Sekunden", "30 Sekunden" }));
        jNotifyDurationBox.setSelectedIndex(1);
        jNotifyDurationBox.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jNotifyDurationBox.toolTipText")); // NOI18N
        jNotifyDurationBox.setMaximumSize(new java.awt.Dimension(105, 18));
        jNotifyDurationBox.setMinimumSize(new java.awt.Dimension(105, 18));
        jNotifyDurationBox.setPreferredSize(new java.awt.Dimension(105, 18));

        jInformOnUpdates.setSelected(true);
        jInformOnUpdates.setText(bundle.getString("DSWorkbenchSettingsDialog.jInformOnUpdates.text")); // NOI18N
        jInformOnUpdates.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jInformOnUpdates.toolTipText")); // NOI18N
        jInformOnUpdates.setMaximumSize(new java.awt.Dimension(105, 18));
        jInformOnUpdates.setMinimumSize(new java.awt.Dimension(105, 18));
        jInformOnUpdates.setOpaque(false);
        jInformOnUpdates.setPreferredSize(new java.awt.Dimension(105, 18));

        jLabel15.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel15.text")); // NOI18N
        jLabel15.setMaximumSize(new java.awt.Dimension(138, 18));
        jLabel15.setMinimumSize(new java.awt.Dimension(138, 18));
        jLabel15.setPreferredSize(new java.awt.Dimension(138, 18));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setOpaque(false);

        jExportTribeNames.setText(bundle.getString("DSWorkbenchSettingsDialog.jExportTribeNames.text")); // NOI18N
        jExportTribeNames.setOpaque(false);

        jExportArriveTime.setText(bundle.getString("DSWorkbenchSettingsDialog.jExportArriveTime.text")); // NOI18N
        jExportArriveTime.setOpaque(false);

        jExportUnit.setText(bundle.getString("DSWorkbenchSettingsDialog.jExportUnit.text")); // NOI18N
        jExportUnit.setOpaque(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jExportTribeNames)
                        .addGap(18, 18, 18)
                        .addComponent(jExportArriveTime))
                    .addComponent(jExportUnit))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jExportTribeNames)
                    .addComponent(jExportArriveTime))
                .addGap(18, 18, 18)
                .addComponent(jExportUnit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel34.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel34.text")); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setOpaque(false);

        jReplaceMarkers.setText(bundle.getString("DSWorkbenchSettingsDialog.jReplaceMarkers.text")); // NOI18N
        jReplaceMarkers.setOpaque(false);

        jReplaceTagMarkers.setText(bundle.getString("DSWorkbenchSettingsDialog.jReplaceTagMarkers.text")); // NOI18N
        jReplaceTagMarkers.setOpaque(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jReplaceMarkers)
                    .addComponent(jReplaceTagMarkers))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jReplaceMarkers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jReplaceTagMarkers)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel39.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel39.text")); // NOI18N

        jLabel16.setText(bundle.getString("DSWorkbenchSettingsDialog.jLabel16.text")); // NOI18N

        jMaxTroopDensity.setText(bundle.getString("DSWorkbenchSettingsDialog.jMaxTroopDensity.text")); // NOI18N

        jButton8.setBackground(new java.awt.Color(239, 235, 223));
        jButton8.setText(bundle.getString("DSWorkbenchSettingsDialog.jButton8.text")); // NOI18N
        jButton8.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jButton8.toolTipText")); // NOI18N
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireSelectTroopsDensityEvent(evt);
            }
        });

        javax.swing.GroupLayout jMiscSettingsLayout = new javax.swing.GroupLayout(jMiscSettings);
        jMiscSettings.setLayout(jMiscSettingsLayout);
        jMiscSettingsLayout.setHorizontalGroup(
            jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMiscSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jMiscSettingsLayout.createSequentialGroup()
                        .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jMiscSettingsLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(25, 25, 25)))
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jNotifyDurationBox, 0, 109, Short.MAX_VALUE)
                        .addComponent(jVillageSortTypeChooser, 0, 109, Short.MAX_VALUE)
                        .addComponent(jInformOnUpdates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jMiscSettingsLayout.createSequentialGroup()
                        .addComponent(jMaxTroopDensity, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)))
                .addContainerGap())
        );
        jMiscSettingsLayout.setVerticalGroup(
            jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMiscSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jVillageSortTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jNotifyDurationBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jInformOnUpdates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jMiscSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jMaxTroopDensity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jSettingsTabbedPane.addTab(bundle.getString("DSWorkbenchSettingsDialog.jMiscSettings.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/res/checkbox.png")), jMiscSettings); // NOI18N

        jOKButton.setBackground(new java.awt.Color(239, 235, 223));
        jOKButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jOKButton.text")); // NOI18N
        jOKButton.setMaximumSize(new java.awt.Dimension(85, 23));
        jOKButton.setMinimumSize(new java.awt.Dimension(85, 23));
        jOKButton.setPreferredSize(new java.awt.Dimension(85, 23));
        jOKButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireOkEvent(evt);
            }
        });

        jCancelButton.setBackground(new java.awt.Color(239, 235, 223));
        jCancelButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jCancelButton.text")); // NOI18N
        jCancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireCloseEvent(evt);
            }
        });

        jCreateAccountButton.setBackground(new java.awt.Color(239, 235, 223));
        jCreateAccountButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jCreateAccountButton.text")); // NOI18N
        jCreateAccountButton.setToolTipText(bundle.getString("DSWorkbenchSettingsDialog.jCreateAccountButton.toolTipText")); // NOI18N
        jCreateAccountButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireCreateAccountEvent(evt);
            }
        });

        jChangePasswordButton.setBackground(new java.awt.Color(239, 235, 223));
        jChangePasswordButton.setText(bundle.getString("DSWorkbenchSettingsDialog.jChangePasswordButton.text")); // NOI18N
        jChangePasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireChangePasswordEvent(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSettingsTabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCreateAccountButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jChangePasswordButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(jCancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSettingsTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCreateAccountButton)
                    .addComponent(jCancelButton)
                    .addComponent(jOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChangePasswordButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void fireUpdateProxySettingsEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireUpdateProxySettingsEvent

        if (jProxyConnectOption.isSelected()) {
            //store properties
            GlobalOptions.addProperty("proxySet", Boolean.toString(true));
            GlobalOptions.addProperty("proxyHost", jProxyHost.getText());
            GlobalOptions.addProperty("proxyPort", jProxyPort.getText());
            GlobalOptions.addProperty("proxyType", Integer.toBinaryString(jProxyTypeChooser.getSelectedIndex()));
            GlobalOptions.addProperty("proxyUser", jProxyUser.getText());
            GlobalOptions.addProperty("proxyPassword", new String(jProxyPassword.getPassword()));
            //create proxy object
            SocketAddress addr = new InetSocketAddress(jProxyHost.getText(), Integer.parseInt(jProxyPort.getText()));
            switch (jProxyTypeChooser.getSelectedIndex()) {
                case 1: {
                    webProxy = new Proxy(Proxy.Type.SOCKS, addr);
                    break;
                }
                default: {
                    webProxy = new Proxy(Proxy.Type.HTTP, addr);
                    break;
                }
            }
            if ((jProxyUser.getText().length() >= 1) && (jProxyPassword.getPassword().length > 1)) {
                Authenticator.setDefault(new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(jProxyUser.getText(), jProxyPassword.getPassword());
                    }
                });
            }
        } else {
            //store properties
            GlobalOptions.addProperty("proxySet", Boolean.toString(false));
            GlobalOptions.addProperty("proxyHost", "");
            GlobalOptions.addProperty("proxyPort", "");
            GlobalOptions.addProperty("proxyType", "");
            GlobalOptions.addProperty("proxyUser", "");
            GlobalOptions.addProperty("proxyPassword", "");
            //set no proxy and no authentification
            Authenticator.setDefault(null);
            webProxy = Proxy.NO_PROXY;
        }

        GlobalOptions.saveProperties();

        checkConnectivity();

        boolean offlineBefore = GlobalOptions.isOfflineMode();

        if (!updateServerList()) {
            //fully failed --> remote update failed and no local servers found
            String message = "Serverliste konnte nicht geladen werden.\n" +
                    "Mögliche Ursachen sind fehlerhafte Netzwerkeinstellungen, keine Verbindung zum Internet\n" +
                    "oder 'dsworkbench.de' ist nicht verfügbar.\n" +
                    "Da noch kein Datenabgleich mit dem Server stattgefunden hat " +
                    "korrigiere bitte deine Netzwerkeinstellungen um diesen einmalig durchzuführen.";
            JOptionPaneHelper.showWarningBox(this, message, "Warnung");
        } else {
            String message = null;
            String title = "Fehler";
            int type = JOptionPane.ERROR_MESSAGE;
            if (offlineBefore) {
                //was offline before checking serverlist
                message = "Die Prüfung der Verbindung zum Internet ist fehlgeschlagen.\n" +
                        "Da du bereits Serverdaten besitzt werden diese verwendet. Für ein Update\n" +
                        "prüfe bitte erneut deine Verbindung zum Internet und deine Netzwerkeinstellungen.";
            } else if (GlobalOptions.isOfflineMode()) {
                //get offline while checking serverlist
                message = "Die Prüfung der Verbindung zum Internet war erfolgreich,\n" +
                        "es konnte dennoch keine aktuelle Serverliste heruntergeladen werden.\n" +
                        "Bitte versuch es später noch einmal.";
            } else {
                //success
                message = "Verbindung erfolgreich hergestellt.";
                title = "Information";
                type = JOptionPane.INFORMATION_MESSAGE;
            }

            //show box
            if (type == JOptionPane.INFORMATION_MESSAGE) {
                JOptionPaneHelper.showInformationBox(this, message, title);
            } else {
                JOptionPaneHelper.showErrorBox(this, message, title);
            }

        }

        DSWorkbenchMainFrame.getSingleton().onlineStateChanged();
    }//GEN-LAST:event_fireUpdateProxySettingsEvent

    private void fireChangeConnectTypeEvent(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fireChangeConnectTypeEvent
        jProxyHost.setEnabled(jProxyConnectOption.isSelected());
        jProxyPort.setEnabled(jProxyConnectOption.isSelected());
        jProxyUser.setEnabled(jProxyConnectOption.isSelected());
        jProxyPassword.setEnabled(jProxyConnectOption.isSelected());
        jProxyTypeChooser.setEnabled(jProxyConnectOption.isSelected());
    }//GEN-LAST:event_fireChangeConnectTypeEvent

    private void fireSelectServerEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireSelectServerEvent
        if (!jSelectServerButton.isEnabled() || jServerList.getSelectedItem() == null) {
            return;
        }

        //save user data for current server
        GlobalOptions.saveUserData();
        String selectedServer = (String) jServerList.getSelectedItem();
        GlobalOptions.addProperty("default.server", selectedServer);
        GlobalOptions.saveProperties();

        GlobalOptions.setSelectedServer(selectedServer);
        updating = true;
        jSelectServerButton.setEnabled(false);
        jDownloadDataButton.setEnabled(false);
        jOKButton.setEnabled(false);
        jCreateAccountButton.setEnabled(false);
        jCancelButton.setEnabled(false);
        jChangePasswordButton.setEnabled(false);
        jTribeNames.setModel(new DefaultComboBoxModel());
        jStatusArea.setText("");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    logger.debug("Start loading from hard disk");
                    boolean ret = DataHolder.getSingleton().loadData(false);
                    logger.debug("Data loaded " + ((ret) ? "successfully" : "with errors"));
                } catch (Exception e) {
                    logger.error("Failed loading data", e);
                }
            }
        });
        logger.debug("Starting update thread");
        t.setDaemon(true);
        t.start();
}//GEN-LAST:event_fireSelectServerEvent

    private void fireCloseEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCloseEvent
        if (!jCancelButton.isEnabled()) {
            return;
        }

        if (!checkTribesAccountSettings()) {
            return;
        }

        DSWorkbenchMainFrame.getSingleton().serverSettingsChangedEvent();
        setVisible(false);
    }//GEN-LAST:event_fireCloseEvent

    private void fireOkEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireOkEvent
        if (!jOKButton.isEnabled()) {
            return;
        }

        /**Validate player settings*/
        String selection = (String) jTribeNames.getSelectedItem();
        if ((selection != null) && (!selection.equals("Bitte wählen"))) {
            logger.debug("Setting default player for server '" + GlobalOptions.getSelectedServer() + "' to " + jTribeNames.getSelectedItem());
            GlobalOptions.addProperty("player." + GlobalOptions.getSelectedServer(), selection);
        }

        /**Update attack vector colors*/
        DefaultTableModel model = ((DefaultTableModel) jAttackColorTable.getModel());
        for (int i = 0; i < model.getRowCount(); i++) {
            String unit = ((UnitHolder) model.getValueAt(i, 0)).getName();
            Color color = (Color) model.getValueAt(i, 1);
            String hexCol = Integer.toHexString(color.getRGB());
            hexCol = "#" + hexCol.substring(2, hexCol.length());
            GlobalOptions.addProperty(unit + ".color", hexCol);
        }

        /**Validate misc properties*/
        int sortType = jVillageSortTypeChooser.getSelectedIndex();
        Village.setOrderType(sortType);
        GlobalOptions.addProperty("import.replace.markers", Boolean.toString(jReplaceMarkers.isSelected()));
        GlobalOptions.addProperty("import.replace.tag.markers", Boolean.toString(jReplaceTagMarkers.isSelected()));

        GlobalOptions.addProperty("village.order", Integer.toString(sortType));
        GlobalOptions.addProperty("notify.duration", Integer.toString(jNotifyDurationBox.getSelectedIndex()));
        GlobalOptions.addProperty("inform.on.updates", Boolean.toString(jInformOnUpdates.isSelected()));
        GlobalOptions.addProperty("export.tribe.names", Boolean.toString(jExportTribeNames.isSelected()));
        GlobalOptions.addProperty("export.units", Boolean.toString(jExportUnit.isSelected()));
        GlobalOptions.addProperty("export.arrive.time", Boolean.toString(jExportArriveTime.isSelected()));
        GlobalOptions.addProperty("show.popup.moral", Boolean.toString(jShowPopupMoral.isSelected()));
        GlobalOptions.addProperty("show.popup.conquers", Boolean.toString(jShowPopupConquers.isSelected()));
        GlobalOptions.addProperty("show.popup.ranks", Boolean.toString(jShowPopupRanks.isSelected()));

        GlobalOptions.saveProperties();
        if (!checkSettings()) {
            return;
        }
        DSWorkbenchMainFrame.getSingleton().serverSettingsChangedEvent();
        setVisible(false);
        MapPanel.getSingleton().getMapRenderer().initiateRedraw(MapRenderer.ALL_LAYERS);
    }//GEN-LAST:event_fireOkEvent

    private void fireSelectGraphicPackEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireSelectGraphicPackEvent
        GlobalOptions.addProperty("default.skin", (String) jGraphicPacks.getSelectedItem());
        try {
            GlobalOptions.loadSkin();
        } catch (Exception e) {
            logger.error("Failed to load skin '" + jGraphicPacks.getSelectedItem() + "'", e);
            JOptionPaneHelper.showErrorBox(this, "Fehler beim laden des Grafikpaketes.", "Fehler");
            //load default
            GlobalOptions.addProperty("default.skin", "default");
            try {
                GlobalOptions.loadSkin();
            } catch (Exception ie) {
                logger.error("Failed to load default skin", ie);
            }
        }
    }//GEN-LAST:event_fireSelectGraphicPackEvent

private void fireCreateAccountEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCreateAccountEvent
    if (!jCreateAccountButton.isEnabled()) {
        return;
    }
    jCreateAccountDialog.setVisible(true);
}//GEN-LAST:event_fireCreateAccountEvent

private void fireLoginIntoAccountEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireLoginIntoAccountEvent
    if (GlobalOptions.isOfflineMode()) {
        JOptionPaneHelper.showInformationBox(this, "Du befindest dich im Offline Modus.\n" +
                "Bitte korrigiere deine Netzwerkeinstellungen und versuche es erneut.", "Offline Modus");
        return;
    }
    String name = jAccountName.getText();
    String password = new String(jAccountPassword.getPassword());
    if ((name != null) && (password != null)) {
        int ret = DatabaseInterface.checkUser(name, password);
        if (ret == DatabaseInterface.ID_SUCCESS) {
            GlobalOptions.addProperty("account.name", jAccountName.getText());
            GlobalOptions.addProperty("account.password", new String(jAccountPassword.getPassword()));
            GlobalOptions.saveProperties();
            JOptionPaneHelper.showInformationBox(this, "Account erfolgreich überprüft.", "Information");
        } else if (ret == DatabaseInterface.ID_DATABASE_CONNECTION_FAILED) {
            JOptionPaneHelper.showErrorBox(this, "Keine Verbindung zur Datenbank.\nBitte versuch es in Kürze noch einmal.", "Fehler");
        } else if (ret == DatabaseInterface.ID_USER_NOT_EXIST) {
            JOptionPaneHelper.showInformationBox(this, "Der Benutzer '" + name + "' existiert nicht oder das Passwort ist falsch.\nBitte überprüfe deine Accounteinstellungen.", "Information");
        } else if (ret == DatabaseInterface.ID_WEB_CONNECTION_FAILED) {
            JOptionPaneHelper.showErrorBox(this, "Es konnte keine Verbindung mit dem Server hergestellt werden.\nBitte überprüfe deine Netzwerkeinstellungen und versuch es in Kürze noch einmal.", "Fehler");
        } else if (ret == DatabaseInterface.ID_QUERY_RETURNED_UNEXPECTED_RESULT) {
            JOptionPaneHelper.showErrorBox(this, "Das Ergebnis der Nutzerprüfung war nicht eindeutig. Bitte kontaktiere den Entwickler.", "Fehler");
        } else if (ret == DatabaseInterface.ID_UNKNOWN_ERROR) {
            JOptionPaneHelper.showErrorBox(this, "Ein unbekannter Fehler ist aufgetreten.\nBitte kontaktiere den Entwickler.", "Fehler");
        }
    }
}//GEN-LAST:event_fireLoginIntoAccountEvent

private void fireClosingEvent(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_fireClosingEvent
    fireCloseEvent(null);
}//GEN-LAST:event_fireClosingEvent

    // <editor-fold defaultstate="collapsed" desc=" Registration EventListeners ">
private void fireRegisterEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireRegisterEvent
        String user = jRegistrationAccountName.getText();
        String password = new String(jRegistrationPassword.getPassword());
        String password2 = new String(jRegistrationPassword2.getPassword());

        if ((user.length() < 3) || (password.length() < 3)) {
            JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Accountname und Passwort müssen mindestens 3 Zeichen lang sein.", "Fehler");
            return;
        }

        if (user.length() > 20) {
            JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Der Accountname darf höchstens 20 Zeichen lang sein.", "Fehler");
            return;
        }

        if (!password.equals(password2)) {
            JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Die eingegebenen Passwörter unterscheiden sich.\nBitte überprüfe deine Eingabe.", "Fehler");
            return;
        }

        int ret = DatabaseInterface.addUser(user, password);
        switch (ret) {
            case DatabaseInterface.ID_DATABASE_CONNECTION_FAILED: {
                JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Fehler beim Verbinden mit der Datenbank.\nBitte versuch es in Kürze noch einmal.", "Fehler");
                break;

            }
            case DatabaseInterface.ID_WEB_CONNECTION_FAILED: {
                JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Es konnte keine Verbindung mit dem Server hergestellt werden.\nBitte überprüfe die Netzwerkeinstellungen und versuch es in Kürze noch einmal.", "Fehler");
                break;

            }
            case DatabaseInterface.ID_USER_ALREADY_EXIST: {
                JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Es existiert bereits ein Benutzer mit dem angegebenen Namen.\nBitte wähle einen anderen Namen.", "Fehler");
                break;
            }
            case DatabaseInterface.ID_QUERY_RETURNED_UNEXPECTED_RESULT: {
                JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Der Benutzer konnte nicht in hinzugefügt werden.\nBitte wende dich an den Entwickler.", "Fehler");
                break;
            }
            case DatabaseInterface.ID_UNKNOWN_ERROR: {
                JOptionPaneHelper.showErrorBox(jCreateAccountDialog, "Ein unbekannter Fehler ist aufgetreten.\nBitte wende dich an den Entwickler.", "Fehler");
                break;
            }
            default: {
                JOptionPaneHelper.showInformationBox(jCreateAccountDialog, "Dein Account wurde erfolgreich angelegt.\nDu kannst nun DS-Serverdaten herunterladen.", "Account angelegt");
                jAccountName.setText(jRegistrationAccountName.getText());
                jAccountPassword.setText(new String(jRegistrationPassword.getPassword()));
                GlobalOptions.addProperty("account.name", user);
                GlobalOptions.addProperty("account.password", password);
                jCreateAccountDialog.setVisible(false);
            }
        }
}//GEN-LAST:event_fireRegisterEvent

private void fireCancelRegistrationEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCancelRegistrationEvent
    jCreateAccountDialog.setVisible(false);
}//GEN-LAST:event_fireCancelRegistrationEvent

    // </editor-fold>
private void fireChangeContinentsOnMinimapEvent(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fireChangeContinentsOnMinimapEvent
        GlobalOptions.addProperty("map.showcontinents", Boolean.toString(jShowContinents.isSelected()));
        MinimapPanel.getSingleton().resetBuffer();
        MinimapPanel.getSingleton().redraw();
}//GEN-LAST:event_fireChangeContinentsOnMinimapEvent

//Download new version of data
private void fireDownloadDataEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireDownloadDataEvent
        if (!jSelectServerButton.isEnabled()) {
            return;
        }

        if (jServerList.getSelectedItem() == null) {
            return;
        }
        // <editor-fold defaultstate="collapsed" desc=" Offline Mode ? ">

        if (GlobalOptions.isOfflineMode()) {
            JOptionPaneHelper.showWarningBox(this, "Du befindest dich im Offline-Modus." +
                    "\nBitte korrigiere deine Netzwerkeinstellungen um den Download durchzuführen.",
                    "Warnung");
            return;
        }

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Account valid, data outdated ? ">
        String selectedServer = (String) jServerList.getSelectedItem();
        String name = GlobalOptions.getProperty("account.name");
        String password = GlobalOptions.getProperty("account.password");
        if (DatabaseInterface.checkUser(name, password) != DatabaseInterface.ID_SUCCESS) {
            JOptionPaneHelper.showErrorBox(this, "Die Accountvalidierung ist fehlgeschlagen.\n" +
                    "Bitte überprüfe deine Account- und Netzwerkeinstellungen und versuches es erneut.",
                    "Fehler");
            return;
        } else {
            long serverDataVersion = DatabaseInterface.getServerDataVersion(selectedServer);
            long userDataVersion = DatabaseInterface.getUserDataVersion(name, selectedServer);
            if (serverDataVersion < 0 || userDataVersion < 0) {
                JOptionPaneHelper.showErrorBox(this, "Fehler bei der Überprüfung der Datenversionen.\n" +
                        "Bitte überprüfe deine Account- und Netzwerkeinstellungen und versuches es erneut.\n" +
                        "Sollte das Problem weiterhin bestehen, kontaktiere bitte den Entwickler.",
                        "Fehler");
                return;
            }
            logger.debug("User data version is " + userDataVersion);
            logger.debug("Server data version is " + serverDataVersion);
            if (userDataVersion == serverDataVersion) {
                JOptionPaneHelper.showInformationBox(this, "Du besitzt bereits die aktuellsten Daten.",
                        "Information");
                return;
            }
        }

        // </editor-fold>

        //save current user data for current server
        GlobalOptions.saveUserData();
        GlobalOptions.setSelectedServer((String) jServerList.getSelectedItem());
        GlobalOptions.addProperty("default.server", GlobalOptions.getSelectedServer());
        GlobalOptions.saveProperties();

        updating = true;
        jSelectServerButton.setEnabled(false);
        jDownloadDataButton.setEnabled(false);
        jOKButton.setEnabled(false);
        jCreateAccountButton.setEnabled(false);
        jCancelButton.setEnabled(false);
        jChangePasswordButton.setEnabled(false);
        jTribeNames.setModel(new DefaultComboBoxModel());
        jStatusArea.setText("");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //clear tribes model due to data is cleared at reload
        jTribeNames.setModel(new DefaultComboBoxModel());

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    logger.debug("Start downloading data");
                    boolean ret = DataHolder.getSingleton().loadData(true);
                    logger.debug("Update finished " + ((ret) ? "successfully" : "with errors"));
                /* if (!ret) {
                logger.info(" - Loading local copy due to update error");
                ret = DataHolder.getSingleton().loadData(false);
                logger.debug("Data loaded " + ((ret) ? "successfully" : "with errors"));
                if (!ret) {
                throw new Exception("Unable to load local data copy");
                }
                }*/
                } catch (Exception e) {
                    logger.error("Failed to load data", e);
                }
            }
        });

        logger.debug("Starting update thread");
        t.setDaemon(true);
        t.start();
}//GEN-LAST:event_fireDownloadDataEvent

    // <editor-fold defaultstate="collapsed" desc=" EventListeners for settings ">
private void fireChangeShowAttackMovementEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fireChangeShowAttackMovementEvent
        GlobalOptions.addProperty("attack.movement", Boolean.toString(jShowAttackMovementBox.isSelected()));
}//GEN-LAST:event_fireChangeShowAttackMovementEvent

private void fireShowSkinPreviewEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireShowSkinPreviewEvent
    String selection = (String) jGraphicPacks.getSelectedItem();
    try {
        if (selection != null) {
            Skin.showPreview(selection, evt.getLocationOnScreen());
        }
    } catch (Exception e) {
        logger.error("No preview available for selected skin '" + selection + "'");
    }
}//GEN-LAST:event_fireShowSkinPreviewEvent

private void fireChangeMarkOwnVillagesOnMinimapEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fireChangeMarkOwnVillagesOnMinimapEvent
    GlobalOptions.addProperty("mark.villages.on.minimap", Boolean.toString(jMarkOwnVillagesOnMinimapBox.isSelected()));
    MinimapPanel.getSingleton().resetBuffer();
    MinimapPanel.getSingleton().redraw();
}//GEN-LAST:event_fireChangeMarkOwnVillagesOnMinimapEvent

private void fireChangeMarkActiveVillageEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fireChangeMarkActiveVillageEvent
    GlobalOptions.addProperty("mark.active.village", Boolean.toString(jMarkActiveVillageBox.isSelected()));
}//GEN-LAST:event_fireChangeMarkActiveVillageEvent

private void fireStandardMarkChangedEvent(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fireStandardMarkChangedEvent
    if (evt.getStateChange() == ItemEvent.SELECTED) {
        int idx = jDefaultMarkBox.getSelectedIndex();
        if (idx < 0) {
            idx = 0;
        }
        GlobalOptions.addProperty("default.mark", Integer.toString(idx));
    }
}//GEN-LAST:event_fireStandardMarkChangedEvent

private void fireShowTroopsTypeEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fireShowTroopsTypeEvent
    GlobalOptions.addProperty("paint.troops.type", Boolean.toString(jTroopsTypeBox.isSelected()));
}//GEN-LAST:event_fireShowTroopsTypeEvent

private void fireDrawAttacksByDefaultChangedEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fireDrawAttacksByDefaultChangedEvent
    GlobalOptions.addProperty("draw.attacks.by.default", Boolean.toString(jDrawAttacksByDefaultBox.isSelected()));
}//GEN-LAST:event_fireDrawAttacksByDefaultChangedEvent

private void fireCheckForUpdatesEvent(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fireCheckForUpdatesEvent
    GlobalOptions.addProperty("check.updates.on.startup", Boolean.toString(jCheckForUpdatesBox.isSelected()));
}//GEN-LAST:event_fireCheckForUpdatesEvent

private void fireChangeShowSectorsEvent(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fireChangeShowSectorsEvent
    GlobalOptions.addProperty("show.sectors", Boolean.toString(jShowSectorsBox.isSelected()));
}//GEN-LAST:event_fireChangeShowSectorsEvent

private void fireShowBarbarianChangedEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fireShowBarbarianChangedEvent
    GlobalOptions.addProperty("show.barbarian", Boolean.toString(jShowBarbarianBox.isSelected()));
}//GEN-LAST:event_fireShowBarbarianChangedEvent

private void fireChangePasswordEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireChangePasswordEvent
    if (!jChangePasswordButton.isEnabled()) {
        return;
    }
    jPasswordChangeAccount.setText(jAccountName.getText());
    jOldPassword.setText("");
    jNewPassword.setText("");
    jNewPassword2.setText("");
    jChangePasswordDialog.setVisible(true);
}//GEN-LAST:event_fireChangePasswordEvent

private void fireDoChangePasswordEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireDoChangePasswordEvent
    String oldPass = new String(jOldPassword.getPassword());
    String newPass = new String(jNewPassword.getPassword());
    String newPass2 = new String(jNewPassword2.getPassword());
    String user = jAccountName.getText();
    if ((newPass.length() < 3) || (newPass2.length() < 3)) {
        JOptionPaneHelper.showInformationBox(jCreateAccountDialog, "Das neue Passwort muss mindestens 3 Zeichen lang sein.", "Fehler");
        return;
    }

    if (!newPass.equals(newPass2)) {
        JOptionPaneHelper.showWarningBox(jCreateAccountDialog, "Die eingegebenen Passwörter unterscheiden sich.\nBitte überprüfe deine Eingabe.", "Warnung");
        return;
    }

    int ret = DatabaseInterface.changePassword(user, oldPass, newPass);

    switch (ret) {
        case DatabaseInterface.ID_DATABASE_CONNECTION_FAILED: {
            JOptionPaneHelper.showErrorBox(jChangePasswordDialog, "Fehler beim Verbinden mit der Datenbank.\nBitte versuch es in Kürze noch einmal.", "Fehler");
            break;

        }
        case DatabaseInterface.ID_WEB_CONNECTION_FAILED: {
            JOptionPaneHelper.showErrorBox(jChangePasswordDialog, "Es konnte keine Verbindung mit dem Server hergestellt werden.\nBitte überprüfe die Netzwerkeinstellungen und versuch es in Kürze noch einmal.", "Fehler");
            break;

        }
        case DatabaseInterface.ID_QUERY_RETURNED_UNEXPECTED_RESULT: {
            JOptionPaneHelper.showErrorBox(jChangePasswordDialog, "Das Passwort konnte nicht geändert werden.\nBitte wende dich an den Entwickler.", "Fehler");
            break;
        }
        case DatabaseInterface.ID_USER_NOT_EXIST: {
            JOptionPaneHelper.showErrorBox(jChangePasswordDialog, "Ein Account mit dem angegebenen Namen und/oder Passwort existiert nicht.", "Fehler");
            break;
        }
        case DatabaseInterface.ID_UNKNOWN_ERROR: {
            JOptionPaneHelper.showErrorBox(jChangePasswordDialog, "Ein unbekannter Fehler ist aufgetreten.\nBitte wende dich an den Entwickler.", "Fehler");
            break;
        }
        default: {
            JOptionPaneHelper.showInformationBox(jChangePasswordDialog, "Das Passwort wurde erfolgreich geändert.", "Passwort geändert");
            jAccountName.setText(user);
            jAccountPassword.setText(newPass);
            GlobalOptions.addProperty("account.password", newPass);
            jChangePasswordDialog.setVisible(false);
        }
    }

}//GEN-LAST:event_fireDoChangePasswordEvent

private void fireCancelChangePasswordEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCancelChangePasswordEvent
    jChangePasswordDialog.setVisible(false);
}//GEN-LAST:event_fireCancelChangePasswordEvent

private void fireAcceptDeffStrengthEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireAcceptDeffStrengthEvent
    if (evt.getSource() == jDeffStrengthOKButton) {
        try {
            double def = 0;
            UnitHolder h = DataHolder.getSingleton().getUnitByPlainName("spear");
            int spear = Integer.parseInt(jSpearAmount.getText());
            def += h.getDefense() * spear;
            int sword = Integer.parseInt(jSwordAmount.getText());
            h = DataHolder.getSingleton().getUnitByPlainName("sword");
            def += h.getDefense() * sword;
            int archer = Integer.parseInt(jArcherAmount.getText());
            h = DataHolder.getSingleton().getUnitByPlainName("archer");
            if (h != null) {
                def += h.getDefense() * archer;
            }
            int heavy = Integer.parseInt(jHeavyAmount.getText());
            h = DataHolder.getSingleton().getUnitByPlainName("heavy");
            def += h.getDefense() * heavy;
            String result = Integer.toString((int) def);
            GlobalOptions.addProperty("max.density.troops", result);
            jMaxTroopDensity.setText(result);
        } catch (Exception e) {
            JOptionPaneHelper.showErrorBox(jTroopDensitySelectionDialog, "Bitte überprüfe deine Eingaben.", "Fehler");
            return;
        }
    }

    jTroopDensitySelectionDialog.setVisible(false);
}//GEN-LAST:event_fireAcceptDeffStrengthEvent

private void fireSelectTroopsDensityEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireSelectTroopsDensityEvent
    jTroopDensitySelectionDialog.setLocationRelativeTo(this);
    jTroopDensitySelectionDialog.setVisible(true);
}//GEN-LAST:event_fireSelectTroopsDensityEvent

    // </editor-fold>
    /**Update the server list*/
    private boolean updateServerList() {
        String[] servers = null;
        //if connection not failed before, get server list
        if (!GlobalOptions.isOfflineMode()) {
            try {
                ServerManager.loadServerList();
                servers = ServerManager.getServerIDs();
                if (servers == null) {
                    throw new Exception("No server received");
                }
            } catch (Exception e) {
                logger.error("Failed to load server list", e);
                GlobalOptions.setOfflineMode(true);
                servers = ServerManager.getLocalServers();
            }
        } else {
            //get local list in offline mode
            servers = ServerManager.getLocalServers();
        }

        if (servers.length < 1) {
            logger.error("Failed to get server list and no locally stored server found");
            jServerList.setModel(new DefaultComboBoxModel());
            jTribeNames.setModel(new DefaultComboBoxModel());
            return false;
        }

        Arrays.sort(servers, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length()) {
                    return -1;
                } else if (o1.length() > o2.length()) {
                    return 1;
                }
                return o1.compareTo(o2);
            }
        });

        DefaultComboBoxModel model = new DefaultComboBoxModel(servers);
        jServerList.setModel(model);

        if (GlobalOptions.getProperty("default.server") != null) {
            if (model.getIndexOf(GlobalOptions.getProperty("default.server")) != -1) {
                jServerList.setSelectedItem(GlobalOptions.getProperty("default.server"));
                model = new DefaultComboBoxModel();
                if (GlobalOptions.getProperty("player." + GlobalOptions.getProperty("default.server")) != null) {
                    model.addElement(GlobalOptions.getProperty("player." + GlobalOptions.getProperty("default.server")));
                    jTribeNames.setModel(model);
                    jTribeNames.setSelectedIndex(0);
                } else {
                    model.addElement("Bitte wählen");
                    jTribeNames.setModel(model);
                    jTribeNames.setSelectedIndex(0);
                }
            } else {
                jServerList.setSelectedIndex(0);
            }
        } else {
            jServerList.setSelectedIndex(0);
        }
        return true;
    }

    /**Check the connectivity to dsworkbench.de*/
    private void checkConnectivity() {
        logger.debug("Checking general connectivity");
        try {
            URLConnection c = new URL("http://www.dsworkbench.de").openConnection(getWebProxy());
            c.setConnectTimeout(10000);
            String header = c.getHeaderField(0);
            if (header != null) {
                logger.debug("Connection established");
                GlobalOptions.setOfflineMode(false);
            } else {
                logger.warn("Could not establish connection");
                GlobalOptions.setOfflineMode(true);
            }
        } catch (Exception in) {
            logger.error("Exception while opening connection", in);
            GlobalOptions.setOfflineMode(true);
        }
    }

    /**Check the tribes server and account*/
    private boolean checkTribesAccountSettings() {
        if (!checkServerPlayerSettings()) {
            String message = "Bitte überprüfe die Spieler-/Servereinstellungen und schließe die Einstellungen mit OK.\n";
            message += "Möglicherweise wurde noch kein Server oder kein Spieler ausgewählt.\n";
            message += "Diese Einstellungen sind für einen korrekten Ablauf zwingend notwendig.";

            if (JOptionPaneHelper.showQuestionConfirmBox(this, message, "Warnung", "Beenden", "Korrigieren") == JOptionPane.NO_OPTION) {
                logger.error("Player/Server settings incorrect. User requested application to terminate");
                System.exit(1);
            }
            return false;
        } else {
            return true;
        }
    }

    /**Check the DS Workbench account*/
    private boolean checkAccountSettings() {
        if (!GlobalOptions.isOfflineMode()) {
            String name = GlobalOptions.getProperty("account.name");
            String password = GlobalOptions.getProperty("account.password");

            boolean noValues = false;
            if (name == null) {
                name = jAccountName.getText();
                if (name == null || name.length() < 1) {
                    noValues = true;
                }
            }
            if (password == null) {
                password = new String(jAccountPassword.getPassword());
                if (password == null || password.length() < 1) {
                    noValues = true;
                }
            }

            //set default to 'user not exist' to get correct message for first start
            int result = DatabaseInterface.ID_USER_NOT_EXIST;
            if (!noValues) {
                //no values set -> should only occur on first start
                result = DatabaseInterface.checkUser(name, password);
            }
            if (result == DatabaseInterface.ID_USER_NOT_EXIST) {
                logger.info("Account check failed (account error)");
                String message = "Die Accountvalidierung ist fehlgeschlagen.\n";
                message += "Wenn du noch nicht registriert bist tu dies bitte über den entsprechenden Button.\n";
                message += "Falls du bereits registriert bist, überprüfe bitte deinen Benutzernamen und dein Passwort.\n";
                message += "Solange die Accountvalidierung nicht durchgeführt wurde, ist es dir nicht möglich sein, Serverdaten zu aktualisieren.";
                if (JOptionPaneHelper.showQuestionConfirmBox(this, message, "Warnung", "Fortfahren", "Einstellungen überprüfen") == JOptionPane.YES_OPTION) {
                    return false;
                }
            } else if (result == DatabaseInterface.ID_WEB_CONNECTION_FAILED) {
                logger.info("Account check failed (connection error)");
                String message = "Die Accountvalidierung ist fehlgeschlagen.\n";
                message += "Bitte überprüfe deine Netzwerkeinstellungen und ob du mit dem Internet verbunden bist.\n";
                message += "Solange die Accountvalidierung nicht erfolgreich war wird es dir nicht möglich sein, Serverdaten zu aktualisieren.";
                if (JOptionPaneHelper.showQuestionConfirmBox(this, message, "Warnung", "Fortfahren", "Einstellungen überprüfen") == JOptionPane.YES_OPTION) {
                    return false;
                }
            } else if (result == DatabaseInterface.ID_SUCCESS) {
                //success, save name and password
                GlobalOptions.addProperty("account.name", name);
                GlobalOptions.addProperty("account.password", password);
                GlobalOptions.saveProperties();
            } else {
                logger.info("Account check failed (other error)");
                String message = "Die Accountvalidierung ist fehlgeschlagen.\n";
                message += "Bitte kontaktiere den Entwickler, da es sich um einen internen Fehler handelt.\n";
                message += "Solange die Accountvalidierung nicht erfolgreich war wird es dir nicht möglich sein, Serverdaten zu aktualisieren.\n" +
                        "Um zu versuchen, das Programm trotzdem zu nutzen, wähle bitte Fortfahren.";
                if (JOptionPaneHelper.showQuestionConfirmBox(this, message, "Warnung", "Fortfahren", "Einstellungen überprüfen") == JOptionPane.YES_OPTION) {
                    return false;
                }
            }
        } else {
            logger.warn("DS Workbench is in offline mode. Account checking not possible.");

            if (JOptionPaneHelper.showWarningConfirmBox(this, "Du befindest dich im Offline-Modus.\n" +
                    "Eine Accountüberprüfung ist daher nicht möglich. Solange dein Account nicht überprüft ist, " +
                    "stehen dir Online-Funktionen nicht zur Verfügung.\n" +
                    "Willst du trotzdem fortfahren?", "Warnung", "Fortfahren", "Einstellungen überprüfen") == JOptionPane.YES_OPTION) {
                //"check settings" pressed
                return false;
            }
        }
        return true;
    }

    @Override
    public void fireDataHolderEvent(String pMessage) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        jStatusArea.insert("(" + f.format(new Date(System.currentTimeMillis())) + ") " + pMessage + "\n", jStatusArea.getText().length());
        try {
            Point point = new Point(0, (int) (jStatusArea.getSize().getHeight()));
            JViewport vp = jScrollPane1.getViewport();
            if ((vp == null) || (point == null)) {
                return;
            }
            vp.setViewPosition(point);
        } catch (Throwable t) {
        }
    }

    @Override
    public void fireDataLoadedEvent(boolean pSuccess) {
        if (pSuccess) {
            Collection<Tribe> tribes = DataHolder.getSingleton().getTribes().values();
            Tribe[] ta = tribes.toArray(new Tribe[]{});
            Arrays.sort(ta, Tribe.CASE_INSENSITIVE_ORDER);
            DefaultComboBoxModel model = new DefaultComboBoxModel();

            model.addElement("Bitte wählen");

            for (Tribe tribe : ta) {
                model.addElement(tribe.toString());
            }
            jTribeNames.setModel(model);

            if (GlobalOptions.getProperty("player." + GlobalOptions.getSelectedServer()) != null) {
                if (model.getIndexOf(GlobalOptions.getProperty("player." + GlobalOptions.getSelectedServer())) != -1) {
                    jTribeNames.setSelectedItem(GlobalOptions.getProperty("player." + GlobalOptions.getSelectedServer()));
                } else {
                    jTribeNames.setSelectedIndex(0);
                }
            } else {
                jTribeNames.setSelectedIndex(0);
            }
            if (DSWorkbenchMainFrame.getSingleton().isInitialized()) {
                DSWorkbenchMainFrame.getSingleton().serverSettingsChangedEvent();
            }
            GlobalOptions.loadUserData();
        }

        updating = false;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jSelectServerButton.setEnabled(true);
        jDownloadDataButton.setEnabled(true);
        jCreateAccountButton.setEnabled(true);
        jOKButton.setEnabled(true);
        jCancelButton.setEnabled(true);
        jChangePasswordButton.setEnabled(true);
    }

    @Override
    public void fireServerChangedEvent() {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup connectionTypeGroup;
    private javax.swing.JTextField jAccountName;
    private javax.swing.JLabel jAccountNameLabel;
    private javax.swing.JPasswordField jAccountPassword;
    private javax.swing.JLabel jAccountPasswordLabel;
    private javax.swing.JTextField jArcherAmount;
    private javax.swing.JTable jAttackColorTable;
    private javax.swing.JLabel jAttackMovementLabel;
    private javax.swing.JLabel jAttackMovementLabel1;
    private javax.swing.JLabel jAttackMovementLabel2;
    private javax.swing.JLabel jAttackMovementLabel3;
    private javax.swing.JPanel jAttackSettings;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jCancelButton;
    private javax.swing.JButton jCancelRegistrationButton;
    private javax.swing.JButton jChangePasswordButton;
    private javax.swing.JDialog jChangePasswordDialog;
    private javax.swing.JButton jCheckAccountButton;
    private javax.swing.JCheckBox jCheckForUpdatesBox;
    private javax.swing.JButton jCreateAccountButton;
    private javax.swing.JDialog jCreateAccountDialog;
    private javax.swing.JComboBox jDefaultMarkBox;
    private javax.swing.JButton jDeffStrengthOKButton;
    private javax.swing.JRadioButton jDirectConnectOption;
    private javax.swing.JButton jDownloadDataButton;
    private javax.swing.JCheckBox jDrawAttacksByDefaultBox;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JCheckBox jExportArriveTime;
    private javax.swing.JCheckBox jExportTribeNames;
    private javax.swing.JCheckBox jExportUnit;
    private javax.swing.JComboBox jGraphicPacks;
    private javax.swing.JTextField jHeavyAmount;
    private javax.swing.JCheckBox jInformOnUpdates;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jLoginPanel;
    private javax.swing.JPanel jMapSettings;
    private javax.swing.JCheckBox jMarkActiveVillageBox;
    private javax.swing.JCheckBox jMarkOwnVillagesOnMinimapBox;
    private javax.swing.JTextField jMaxTroopDensity;
    private javax.swing.JPanel jMiscSettings;
    private javax.swing.JPanel jNetworkSettings;
    private javax.swing.JPasswordField jNewPassword;
    private javax.swing.JPasswordField jNewPassword2;
    private javax.swing.JComboBox jNotifyDurationBox;
    private javax.swing.JButton jOKButton;
    private javax.swing.JPasswordField jOldPassword;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jPasswordChangeAccount;
    private javax.swing.JPanel jPlayerServerSettings;
    private javax.swing.JButton jPreviewSkinButton;
    private javax.swing.JLabel jProxyAdressLabel;
    private javax.swing.JRadioButton jProxyConnectOption;
    private javax.swing.JTextField jProxyHost;
    private javax.swing.JPasswordField jProxyPassword;
    private javax.swing.JTextField jProxyPort;
    private javax.swing.JLabel jProxyPortLabel;
    private javax.swing.JComboBox jProxyTypeChooser;
    private javax.swing.JTextField jProxyUser;
    private javax.swing.JButton jRefeshNetworkButton;
    private javax.swing.JButton jRegisterButton;
    private javax.swing.JLabel jRegisterNameLabel;
    private javax.swing.JLabel jRegisterPasswordLabel;
    private javax.swing.JTextField jRegistrationAccountName;
    private javax.swing.JPasswordField jRegistrationPassword;
    private javax.swing.JPasswordField jRegistrationPassword2;
    private javax.swing.JLabel jRepeatPasswordLabel;
    private javax.swing.JCheckBox jReplaceMarkers;
    private javax.swing.JCheckBox jReplaceTagMarkers;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jSelectServerButton;
    private javax.swing.JButton jSelectSkinButton;
    private javax.swing.JComboBox jServerList;
    private javax.swing.JTabbedPane jSettingsTabbedPane;
    private javax.swing.JCheckBox jShowAttackMovementBox;
    private javax.swing.JCheckBox jShowBarbarianBox;
    private javax.swing.JCheckBox jShowContinents;
    private javax.swing.JLabel jShowContinentsLabel;
    private javax.swing.JCheckBox jShowPopupConquers;
    private javax.swing.JCheckBox jShowPopupMoral;
    private javax.swing.JCheckBox jShowPopupRanks;
    private javax.swing.JCheckBox jShowSectorsBox;
    private javax.swing.JLabel jSkinPackLabel;
    private javax.swing.JTextField jSpearAmount;
    private javax.swing.JTextArea jStatusArea;
    private javax.swing.JTextField jSwordAmount;
    private javax.swing.JComboBox jTribeNames;
    private javax.swing.JDialog jTroopDensitySelectionDialog;
    private javax.swing.JCheckBox jTroopsTypeBox;
    private javax.swing.JComboBox jVillageSortTypeChooser;
    private javax.swing.ButtonGroup tagMarkerGroup;
    // End of variables declaration//GEN-END:variables
}

