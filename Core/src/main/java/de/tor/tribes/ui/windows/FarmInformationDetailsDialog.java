/* 
 * Copyright 2015 Torridity.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tor.tribes.ui.windows;

import de.tor.tribes.types.FarmInformation;
import de.tor.tribes.ui.views.DSWorkbenchFarmManager;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Torridity
 */
public class FarmInformationDetailsDialog extends javax.swing.JDialog {

    private FarmInformation currentInfo = null;

    /**
     * Creates new form FarmInformationDetailsDialog
     */
    public FarmInformationDetailsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setupAndShow(FarmInformation pInfo) {
        if (pInfo == null) {
            return;
        }
        currentInfo = pInfo;
        setTitle("Farminformationen - " + pInfo.getVillage().getFullName());
        jVillageName.setText(pInfo.getVillage().getFullName());
        jAttacks.setText(Integer.toString(pInfo.getAttackCount()));
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        if (pInfo.getLastReport() > 0) {
            jLastReport.setText(f.format(pInfo.getLastReport()));
        } else {
            jLastReport.setText("Noch kein Bericht eingelesen");
        }

        jWoodLevel.setText(nf.format(pInfo.getWoodLevel()));
        jClayLevel.setText(nf.format(pInfo.getClayLevel()));
        jIronLevel.setText(nf.format(pInfo.getIronLevel()));
        jStorageLevel.setText(nf.format(pInfo.getStorageLevel()));
        jHideLevel.setText(nf.format(pInfo.getHideLevel()));

        jCurrentWood.setText(nf.format(pInfo.getWoodInStorage()));
        jCurrentClay.setText(nf.format(pInfo.getClayInStorage()));
        jCurrentIron.setText(nf.format(pInfo.getIronInStorage()));

        jFarmedWood.setText(nf.format(pInfo.getHauledWood()));
        jFarmedClay.setText(nf.format(pInfo.getHauledClay()));
        jFarmedIron.setText(nf.format(pInfo.getHauledIron()));

        jTextPane1.setText((pInfo.getLastSendInformation() != null) ? pInfo.getLastSendInformation() : "Keine Informationen vorhanden");

        pack();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jVillageName = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jStatusPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jAttacks = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLastReport = new javax.swing.JLabel();
        jBuildingPanel = new javax.swing.JPanel();
        jClayLevel = new com.jidesoft.swing.LabeledTextField();
        jWoodLevel = new com.jidesoft.swing.LabeledTextField();
        jIronLevel = new com.jidesoft.swing.LabeledTextField();
        jStorageLevel = new com.jidesoft.swing.LabeledTextField();
        jHideLevel = new com.jidesoft.swing.LabeledTextField();
        jButton2 = new javax.swing.JButton();
        jCurrentResourcesPanel = new javax.swing.JPanel();
        jCurrentClay = new com.jidesoft.swing.LabeledTextField();
        jCurrentWood = new com.jidesoft.swing.LabeledTextField();
        jCurrentIron = new com.jidesoft.swing.LabeledTextField();
        jOverallInfoPanel = new javax.swing.JPanel();
        jFarmedWood = new com.jidesoft.swing.LabeledTextField();
        jFarmedClay = new com.jidesoft.swing.LabeledTextField();
        jFarmedIron = new com.jidesoft.swing.LabeledTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(370, 600));
        setPreferredSize(new java.awt.Dimension(370, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Dorfname");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        jVillageName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jVillageName.setText("Irgendein Dorf (232|234) K23");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jVillageName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        getContentPane().add(jSeparator1, gridBagConstraints);

        jStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Aktueller Status"));
        jStatusPanel.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Bisherige Angriffe");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jStatusPanel.add(jLabel3, gridBagConstraints);

        jAttacks.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jStatusPanel.add(jAttacks, gridBagConstraints);

        jLabel5.setText("Letzter Bericht");
        jLabel5.setMaximumSize(new java.awt.Dimension(85, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(85, 14));
        jLabel5.setPreferredSize(new java.awt.Dimension(85, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jStatusPanel.add(jLabel5, gridBagConstraints);

        jLastReport.setText("Kein Bericht vorhanden");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jStatusPanel.add(jLastReport, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jStatusPanel, gridBagConstraints);

        jBuildingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Gebäudeinformationen"));
        jBuildingPanel.setLayout(new java.awt.GridBagLayout());

        jClayLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/lehm.png"))); // NOI18N
        jClayLevel.setMinimumSize(new java.awt.Dimension(50, 24));
        jClayLevel.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jBuildingPanel.add(jClayLevel, gridBagConstraints);

        jWoodLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/holz.png"))); // NOI18N
        jWoodLevel.setMinimumSize(new java.awt.Dimension(50, 24));
        jWoodLevel.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jBuildingPanel.add(jWoodLevel, gridBagConstraints);

        jIronLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/eisen.png"))); // NOI18N
        jIronLevel.setMinimumSize(new java.awt.Dimension(50, 24));
        jIronLevel.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jBuildingPanel.add(jIronLevel, gridBagConstraints);

        jStorageLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/res.png"))); // NOI18N
        jStorageLevel.setMinimumSize(new java.awt.Dimension(50, 24));
        jStorageLevel.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jBuildingPanel.add(jStorageLevel, gridBagConstraints);

        jHideLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/hide.png"))); // NOI18N
        jHideLevel.setMinimumSize(new java.awt.Dimension(50, 24));
        jHideLevel.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jBuildingPanel.add(jHideLevel, gridBagConstraints);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/refresh.png"))); // NOI18N
        jButton2.setToolTipText("<html>Aktuelle Rohstoffgeb&auml;ude und Speichergr&ouml;&szlig;e errechnen<br/>Diese Funktion wird versuchen, mit Hilfe vorhandener Berichte die aktuellen Geb&auml;deinformationen zu bestimmen.<br/>Vorrangig ist dies f&uuml;r Server gedacht, auf denen keine Geb&auml;udeinformationen ersp&auml;ht werden k&ouml;nnen.<br/>\nAuf anderen Servern sollte man diese Funktion nicht verwenden.</html>");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fireSetBuildingsEvent(evt);
            }
        });
        jBuildingPanel.add(jButton2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jBuildingPanel, gridBagConstraints);

        jCurrentResourcesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Aktuell vorhandene Rohstoffe"));
        jCurrentResourcesPanel.setLayout(new java.awt.GridBagLayout());

        jCurrentClay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/lehm.png"))); // NOI18N
        jCurrentClay.setMinimumSize(new java.awt.Dimension(100, 24));
        jCurrentClay.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jCurrentResourcesPanel.add(jCurrentClay, gridBagConstraints);

        jCurrentWood.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/holz.png"))); // NOI18N
        jCurrentWood.setMinimumSize(new java.awt.Dimension(100, 24));
        jCurrentWood.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jCurrentResourcesPanel.add(jCurrentWood, gridBagConstraints);

        jCurrentIron.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/eisen.png"))); // NOI18N
        jCurrentIron.setMinimumSize(new java.awt.Dimension(100, 24));
        jCurrentIron.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jCurrentResourcesPanel.add(jCurrentIron, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jCurrentResourcesPanel, gridBagConstraints);

        jOverallInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bisher gefarmte Rohstoffe"));
        jOverallInfoPanel.setLayout(new java.awt.GridBagLayout());

        jFarmedWood.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/holz.png"))); // NOI18N
        jFarmedWood.setMinimumSize(new java.awt.Dimension(100, 24));
        jFarmedWood.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jOverallInfoPanel.add(jFarmedWood, gridBagConstraints);

        jFarmedClay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/lehm.png"))); // NOI18N
        jFarmedClay.setMinimumSize(new java.awt.Dimension(100, 24));
        jFarmedClay.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jOverallInfoPanel.add(jFarmedClay, gridBagConstraints);

        jFarmedIron.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/eisen.png"))); // NOI18N
        jFarmedIron.setMinimumSize(new java.awt.Dimension(100, 24));
        jFarmedIron.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jOverallInfoPanel.add(jFarmedIron, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jOverallInfoPanel, gridBagConstraints);

        jButton1.setText("Schließen");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireCloseDialogEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Informationen zum letzten Farmversuch"));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(8, 80));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(8, 80));
        jScrollPane1.setViewportView(jTextPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fireCloseDialogEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCloseDialogEvent
        dispose();
    }//GEN-LAST:event_fireCloseDialogEvent

    private void fireSetBuildingsEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireSetBuildingsEvent
        if (currentInfo != null) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(0);
            nf.setMaximumFractionDigits(0);
            currentInfo.guessBuildings();
            jWoodLevel.setText(nf.format(currentInfo.getWoodLevel()));
            jClayLevel.setText(nf.format(currentInfo.getClayLevel()));
            jIronLevel.setText(nf.format(currentInfo.getIronLevel()));
            jStorageLevel.setText(nf.format(currentInfo.getStorageLevel()));
        }
        DSWorkbenchFarmManager.getSingleton().repaint();
    }//GEN-LAST:event_fireSetBuildingsEvent
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jAttacks;
    private javax.swing.JPanel jBuildingPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.jidesoft.swing.LabeledTextField jClayLevel;
    private com.jidesoft.swing.LabeledTextField jCurrentClay;
    private com.jidesoft.swing.LabeledTextField jCurrentIron;
    private javax.swing.JPanel jCurrentResourcesPanel;
    private com.jidesoft.swing.LabeledTextField jCurrentWood;
    private com.jidesoft.swing.LabeledTextField jFarmedClay;
    private com.jidesoft.swing.LabeledTextField jFarmedIron;
    private com.jidesoft.swing.LabeledTextField jFarmedWood;
    private com.jidesoft.swing.LabeledTextField jHideLevel;
    private com.jidesoft.swing.LabeledTextField jIronLevel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLastReport;
    private javax.swing.JPanel jOverallInfoPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jStatusPanel;
    private com.jidesoft.swing.LabeledTextField jStorageLevel;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel jVillageName;
    private com.jidesoft.swing.LabeledTextField jWoodLevel;
    // End of variables declaration//GEN-END:variables
}
