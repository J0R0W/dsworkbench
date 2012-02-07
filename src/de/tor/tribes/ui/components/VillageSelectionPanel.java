/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VillageSelectionPanel.java
 *
 * Created on 22.12.2011, 11:04:40
 */
package de.tor.tribes.ui.components;

import de.tor.tribes.io.DataHolder;
import de.tor.tribes.io.UnitHolder;
import de.tor.tribes.types.ext.Ally;
import de.tor.tribes.types.Tag;
import de.tor.tribes.types.ext.Tribe;
import de.tor.tribes.types.ext.Village;
import de.tor.tribes.ui.renderer.UnitListCellRenderer;
import de.tor.tribes.util.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXList;

/**
 *
 * @author jejkal
 */
public class VillageSelectionPanel extends javax.swing.JPanel {

    public enum SELECTION_ELEMENT {

        ALLY, TRIBE, GROUP, CONTINENT, VILLAGE
    }
    private IconizedList allyList = null;
    private IconizedList tribeList = null;
    private IconizedList groupList = null;
    private IconizedList continentList = null;
    private IconizedList villageList = null;
    private FilterPipeline<Ally, Tribe> allyTribePipeline = null;
    private FilterPipeline<Tribe, GroupSelectionList.ListItem> tribeGroupPipeline = null;
    private FilterPipeline<GroupSelectionList.ListItem, ContinentVillageSelection> groupContinentPipeline = null;
    private FilterPipeline<ContinentVillageSelection, Village> continentVillagePipeline = null;
    private VillageSelectionPanelListener listener = null;

    /**
     * Creates new form VillageSelectionPanel
     */
    public VillageSelectionPanel(VillageSelectionPanelListener pListener) {
        initComponents();
        listener = pListener;
        allyList = new IconizedList("/res/awards/ally.png");
        jAllyScrollPane.setViewportView(allyList);

        tribeList = new IconizedList("/res/awards/tribe.png");
        jTribeScrollPane.setViewportView(tribeList);

        groupList = new GroupSelectionList("/res/awards/group.png");
        jGroupScrollPane.setViewportView(groupList);

        continentList = new IconizedList("/res/awards/continent.png");
        jContinentScrollPane.setViewportView(continentList);

        villageList = new IconizedList("/res/awards/village.png");
        jVillageScrollPane.setViewportView(villageList);
        enableSelectionElement(SELECTION_ELEMENT.ALLY, true);
        enableSelectionElement(SELECTION_ELEMENT.TRIBE, true);
        enableSelectionElement(SELECTION_ELEMENT.GROUP, true);
        enableSelectionElement(SELECTION_ELEMENT.CONTINENT, true);
        enableSelectionElement(SELECTION_ELEMENT.VILLAGE, true);
        setUnitSelectionEnabled(false);
        setFakeSelectionEnabled(false);
        setAmountSelectionEnabled(false);
    }

    public void setup() {
        allyList.setListData(AllyUtils.getAlliesByFilter("", Ally.CASE_INSENSITIVE_ORDER));
        jXTextField1.setText("");

        setupFilters();
        //do initial selection
        if (!allyList.isVisible()) {
            allyList.setSelectedValue(GlobalOptions.getSelectedProfile().getTribe().getAlly(), false);
        }

        if (!tribeList.isVisible()) {
            tribeList.setSelectedValue(GlobalOptions.getSelectedProfile().getTribe(), false);
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (UnitHolder unit : DataHolder.getSingleton().getUnits()) {
            if (!unit.getPlainName().equals("militia")) {
                model.addElement(unit);
            }
        }

        jUnitBox.setModel(model);
        jUnitBox.setRenderer(new UnitListCellRenderer());
        jUnitBox.setSelectedItem(DataHolder.getSingleton().getUnitByPlainName("ram"));
    }

    private void setupFilters() {
        allyTribePipeline = new FilterPipeline<Ally, Tribe>(allyList, tribeList) {

            @Override
            public Tribe[] filter() {
                List<Tribe> res = new LinkedList<Tribe>();
                for (Object o : getSelection()) {
                    Ally a = (Ally) o;
                    for (Tribe t : a.getTribes()) {
                        res.add(t);
                    }
                }

                Collections.sort(res, Tribe.CASE_INSENSITIVE_ORDER);
                return res.toArray(new Tribe[res.size()]);
            }
        };

        tribeGroupPipeline = new FilterPipeline<Tribe, GroupSelectionList.ListItem>(tribeList, groupList) {

            @Override
            public GroupSelectionList.ListItem[] filter() {
                List<Tag> usedTags = new LinkedList<Tag>();
                List<Village> villages = new LinkedList<Village>();
                List<GroupSelectionList.ListItem> items = new LinkedList<GroupSelectionList.ListItem>();
                for (Object o : getSelection()) {
                    Tribe t = (Tribe) o;
                    Collections.addAll(villages, t.getVillageList());
                    for (Tag tag : TagUtils.getTagsByTribe(t, null)) {
                        if (!usedTags.contains(tag)) {
                            items.add(new GroupSelectionList.ListItem(tag));
                            usedTags.add(tag);
                        }
                    }
                }
                ((GroupSelectionList) getOutputList()).setRelevantVillages(villages.toArray(new Village[villages.size()]));
                return items.toArray(new GroupSelectionList.ListItem[items.size()]);
            }
        };

        groupContinentPipeline = new FilterPipeline<GroupSelectionList.ListItem, ContinentVillageSelection>(groupList, continentList) {

            @Override
            public ContinentVillageSelection[] filter() {
                HashMap<Integer, ContinentVillageSelection> map = new HashMap<Integer, ContinentVillageSelection>();

                for (Village v : ((GroupSelectionList) getInputList()).getValidVillages()) {
                    int cont = v.getContinent();
                    ContinentVillageSelection s = map.get(cont);
                    if (s == null) {
                        s = new ContinentVillageSelection(cont);
                        map.put(cont, s);
                    }
                    s.addVillage(v);
                }

                ContinentVillageSelection[] result = map.values().toArray(new ContinentVillageSelection[map.size()]);
                Arrays.sort(result, new Comparator<ContinentVillageSelection>() {

                    @Override
                    public int compare(ContinentVillageSelection o1, ContinentVillageSelection o2) {
                        return String.CASE_INSENSITIVE_ORDER.compare(o1.toString(), o2.toString());
                    }
                });
                return result;
            }

            @Override
            public void updateOutputSelection() {
                getOutputList().getSelectionModel().setSelectionInterval(0, getOutputList().getElementCount() - 1);
            }
        };


        continentVillagePipeline = new FilterPipeline<ContinentVillageSelection, Village>(continentList, villageList) {

            @Override
            public Village[] filter() {
                List<Village> res = new LinkedList<Village>();
                for (Object o : getSelection()) {
                    ContinentVillageSelection c = (ContinentVillageSelection) o;
                    Collections.addAll(res, c.getVillages());
                }
                Collections.sort(res, Village.CASE_INSENSITIVE_ORDER);
                return res.toArray(new Village[res.size()]);
            }

            @Override
            public void updateOutputSelection() {
                getOutputList().getSelectionModel().setSelectionInterval(0, getOutputList().getElementCount() - 1);
            }
        };
    }

    public final void setUnitSelectionEnabled(boolean pValue) {
        jUnitBox.setVisible(pValue);
    }

    public final void setFakeSelectionEnabled(boolean pValue) {
        jFakeBox.setVisible(pValue);
    }

    public final void setAmountSelectionEnabled(boolean pValue) {
        jAmountLabel.setVisible(pValue);
        jAmountField.setVisible(pValue);
    }

    public final void enableSelectionElement(SELECTION_ELEMENT pElement, boolean pEnable) {
        switch (pElement) {
            case ALLY:
                changeSelectionElementVisibility(jAllyScrollPane, allyList, pEnable);
                jXTextField1.setVisible(pEnable);
                break;
            case TRIBE:
                changeSelectionElementVisibility(jTribeScrollPane, tribeList, pEnable);
                break;
            case GROUP:
                changeSelectionElementVisibility(jGroupScrollPane, groupList, pEnable);
                jLabel2.setVisible(pEnable);
                break;
            case CONTINENT:
                changeSelectionElementVisibility(jContinentScrollPane, continentList, pEnable);
                break;
            case VILLAGE:
                changeSelectionElementVisibility(jVillageScrollPane, villageList, pEnable);
                break;
        }
    }

    private void changeSelectionElementVisibility(JScrollPane pScrollPane, JXList pList, boolean pShow) {
        pScrollPane.setVisible(pShow);
        pList.setVisible(pShow);
    }

    public static interface VillageSelectionPanelListener {

        public void fireVillageSelectionEvent(Village[] pSelection);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jAllyScrollPane = new javax.swing.JScrollPane();
        jTribeScrollPane = new javax.swing.JScrollPane();
        jGroupScrollPane = new javax.swing.JScrollPane();
        jContinentScrollPane = new javax.swing.JScrollPane();
        jVillageScrollPane = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jXButton1 = new org.jdesktop.swingx.JXButton();
        jUnitBox = new org.jdesktop.swingx.JXComboBox();
        jFakeBox = new javax.swing.JCheckBox();
        jXTextField1 = new org.jdesktop.swingx.JXTextField();
        jAmountLabel = new javax.swing.JLabel();
        jAmountField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(jAllyScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTribeScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jGroupScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jContinentScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jVillageScrollPane, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("<html>Elemente der Reihe nach ausw&auml;hlen. F&uuml;r eine Suche nach Elementen in die entsprechende Liste klicken und den Elementnamen tippen oder per STRG+F die Suche &ouml;ffnen. Mehrere Elemente mit gedr&uuml;ckter STRG-Taste ausw&auml;hlen.</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("<html>Gruppeneintrag doppelt klicken, um Art der Verkn&uuml;pfung zu &auml;ndern. Verwendung einer Gruppe &uuml;ber <b>ENTF</b> l&ouml;schen.</html>");
        jLabel2.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator1, gridBagConstraints);

        jXButton1.setText("Auswahl verwenden");
        jXButton1.setToolTipText("Fügt alle gewählten Dörfer ein.");
        jXButton1.setMaximumSize(new java.awt.Dimension(90, 23));
        jXButton1.setMinimumSize(new java.awt.Dimension(90, 23));
        jXButton1.setPreferredSize(new java.awt.Dimension(90, 23));
        jXButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireTransferVillageSelectionEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jXButton1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jUnitBox, gridBagConstraints);

        jFakeBox.setText("Als Fake einfügen");
        jFakeBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/no_fake.png"))); // NOI18N
        jFakeBox.setMaximumSize(new java.awt.Dimension(80, 27));
        jFakeBox.setMinimumSize(new java.awt.Dimension(80, 27));
        jFakeBox.setPreferredSize(new java.awt.Dimension(80, 27));
        jFakeBox.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/fake.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jFakeBox, gridBagConstraints);

        jXTextField1.setPrompt("Name/Tag eingeben");
        jXTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                fireAllyNameTagChangedEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jXTextField1, gridBagConstraints);

        jAmountLabel.setText("Anzahl");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jAmountLabel, gridBagConstraints);

        jAmountField.setText("1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jAmountField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void fireTransferVillageSelectionEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireTransferVillageSelectionEvent
        List<Village> result = new LinkedList<Village>();
        Object[] selection = villageList.getSelectedValues();
        if (selection == null || selection.length == 0) {
            //transfer all
            if (villageList.getElementCount() != 0) {
                for (int i = 0; i < villageList.getElementCount(); i++) {
                    result.add((Village) villageList.getElementAt(i));
                }
            }
        } else {
            for (Object s : selection) {
                int cnt = 1;
                if (jAmountField.isVisible()) {
                    cnt = UIHelper.parseIntFromField(jAmountField, 1);
                }
                for (int i = 0; i < cnt; i++) {
                    result.add((Village) s);
                }
            }
        }

        if (!result.isEmpty()) {
            listener.fireVillageSelectionEvent(result.toArray(new Village[result.size()]));
        }

    }//GEN-LAST:event_fireTransferVillageSelectionEvent

    private void fireAllyNameTagChangedEvent(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_fireAllyNameTagChangedEvent
        allyList.setListData(AllyUtils.getAlliesByFilter(jXTextField1.getText(), Ally.CASE_INSENSITIVE_ORDER));
    }//GEN-LAST:event_fireAllyNameTagChangedEvent

    public UnitHolder getSelectedUnit() {
        return (UnitHolder) jUnitBox.getSelectedItem();
    }

    public boolean isFake() {
        return jFakeBox.isSelected();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jAllyScrollPane;
    private javax.swing.JTextField jAmountField;
    private javax.swing.JLabel jAmountLabel;
    private javax.swing.JScrollPane jContinentScrollPane;
    private javax.swing.JCheckBox jFakeBox;
    private javax.swing.JScrollPane jGroupScrollPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JScrollPane jTribeScrollPane;
    private org.jdesktop.swingx.JXComboBox jUnitBox;
    private javax.swing.JScrollPane jVillageScrollPane;
    private org.jdesktop.swingx.JXButton jXButton1;
    private org.jdesktop.swingx.JXTextField jXTextField1;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
        Logger.getRootLogger().addAppender(new ConsoleAppender(new org.apache.log4j.PatternLayout("%d - %-5p - %-20c (%C [%L]) - %m%n")));
        GlobalOptions.setSelectedServer("de43");
        DataHolder.getSingleton().loadData(false);
        ProfileManager.getSingleton().loadProfiles();
        GlobalOptions.setSelectedProfile(ProfileManager.getSingleton().getProfiles("de43")[0]);
        GlobalOptions.loadUserData();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        VillageSelectionPanel p = new VillageSelectionPanel(new VillageSelectionPanelListener() {

            @Override
            public void fireVillageSelectionEvent(Village[] pSelection) {
                System.out.println("TRANS " + pSelection.length);
            }
        });
        p.setup();
        f.getContentPane().add(p);
        f.pack();
        f.setVisible(true);

    }
}

abstract class FilterPipeline<C, D> {

    private JXList inputList = null;
    private JXList outputList = null;

    public FilterPipeline(JXList pThisList, JXList pRightList) {
        inputList = pThisList;
        outputList = pRightList;
        pThisList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    outputList.setListData(filter());
                    updateOutputSelection();
                }
            }
        });
    }

    /**
     * Special constructor for group list, as selection handling is managed by the list itself
     */
    public FilterPipeline(GroupSelectionList pThisList, JXList pRightList) {
        inputList = pThisList;
        outputList = pRightList;
    }

    public Object[] getSelection() {
        List<C> result = new LinkedList<C>();
        if (inputList.isVisible() || inputList.getSelectedValues().length > 0) {
            for (Object o : inputList.getSelectedValues()) {
                result.add((C) o);
            }
        } else {
            for (int i = 0; i < inputList.getModel().getSize(); i++) {
                result.add((C) inputList.getModel().getElementAt(i));
            }
        }

        return result.toArray();
    }

    public JXList getInputList() {
        return inputList;
    }

    public JXList getOutputList() {
        return outputList;
    }

    public abstract D[] filter();

    public void updateOutputSelection() {
        if (outputList.isVisible()) {
            outputList.setSelectedIndex(0);
        } else {
            outputList.getSelectionModel().setSelectionInterval(0, outputList.getElementCount() - 1);
        }
    }
}

class ContinentVillageSelection {

    private int continent = 0;
    private String continentString = null;
    private List<Village> villages = null;

    public ContinentVillageSelection(int pContinent) {
        continent = pContinent;
        continentString = "K" + ((continent < 10) ? "0" + continent : continent);
        villages = new LinkedList<Village>();
    }

    public void addVillage(Village pVillage) {
        villages.add(pVillage);
    }

    public Village[] getVillages() {
        return villages.toArray(new Village[villages.size()]);
    }

    @Override
    public String toString() {
        return continentString;
    }
}