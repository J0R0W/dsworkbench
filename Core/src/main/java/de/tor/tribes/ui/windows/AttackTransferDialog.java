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

import de.tor.tribes.types.Attack;
import de.tor.tribes.util.attack.AttackManager;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Torridity
 */
public class AttackTransferDialog extends javax.swing.JDialog {

    private List<Attack> attacks = null;

    /**
     * Creates new form AttackTransferDialog
     */
    public AttackTransferDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setupAndShow(Attack[] pAttacks) {
        attacks = Arrays.asList(pAttacks);
        jExistingPlanBox.setModel(new DefaultComboBoxModel(AttackManager.getSingleton().getGroups()));
        pack();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel14 = new javax.swing.JLabel();
        jExistingPlanBox = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jNewPlanName = new org.jdesktop.swingx.JXTextField();
        jCancelTransferButton = new javax.swing.JButton();
        jDoTransferButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Befehle übertragen");
        setMinimumSize(new java.awt.Dimension(290, 140));
        setPreferredSize(new java.awt.Dimension(290, 140));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel14.setText("Vorhandener Plan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel14, gridBagConstraints);

        jExistingPlanBox.setMinimumSize(new java.awt.Dimension(200, 25));
        jExistingPlanBox.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jExistingPlanBox, gridBagConstraints);

        jLabel15.setText("Neuer Plan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel15, gridBagConstraints);

        jNewPlanName.setMinimumSize(new java.awt.Dimension(200, 25));
        jNewPlanName.setPreferredSize(new java.awt.Dimension(200, 25));
        jNewPlanName.setPrompt("Bei Bedarf Name eingeben");
        jNewPlanName.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jNewPlanNamefireNewResultTargetPlanChangedEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jNewPlanName, gridBagConstraints);

        jCancelTransferButton.setText("Abbrechen");
        jCancelTransferButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jCancelTransferButtonfireTransferResultsEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jCancelTransferButton, gridBagConstraints);

        jDoTransferButton.setText("OK");
        jDoTransferButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jDoTransferButtonfireTransferResultsEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jDoTransferButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jNewPlanNamefireNewResultTargetPlanChangedEvent(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jNewPlanNamefireNewResultTargetPlanChangedEvent
        boolean enableExisting = !(jNewPlanName.getText() != null && !jNewPlanName.getText().isEmpty());
        jExistingPlanBox.setEnabled(enableExisting);
        jLabel14.setEnabled(enableExisting);
    }//GEN-LAST:event_jNewPlanNamefireNewResultTargetPlanChangedEvent

    private void jCancelTransferButtonfireTransferResultsEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCancelTransferButtonfireTransferResultsEvent
        if (evt.getSource() == jDoTransferButton) {

            String plan = null;
            if (jExistingPlanBox.isEnabled()) {
                plan = (String) jExistingPlanBox.getSelectedItem();
            } else {
                plan = jNewPlanName.getText();
                AttackManager.getSingleton().addGroup(plan);
            }

            AttackManager.getSingleton().invalidate();
            for (Attack a : attacks) {
                AttackManager.getSingleton().addManagedElement(plan, a);
            }
            AttackManager.getSingleton().revalidate(plan, true);


        }
        setVisible(false);
    }//GEN-LAST:event_jCancelTransferButtonfireTransferResultsEvent

    private void jDoTransferButtonfireTransferResultsEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDoTransferButtonfireTransferResultsEvent
        if (evt.getSource() == jDoTransferButton) {

            String plan = null;
            if (jExistingPlanBox.isEnabled()) {
                plan = (String) jExistingPlanBox.getSelectedItem();
            } else {
                plan = jNewPlanName.getText();
                AttackManager.getSingleton().addGroup(plan);
            }

            AttackManager.getSingleton().invalidate();
            for (Attack a : attacks) {
                AttackManager.getSingleton().addManagedElement(plan, a);
            }
            AttackManager.getSingleton().revalidate(plan, true);
        }
        setVisible(false);
    }//GEN-LAST:event_jDoTransferButtonfireTransferResultsEvent

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | javax.swing.UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException ex) {
            java.util.logging.Logger.getLogger(AttackTransferDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AttackTransferDialog dialog = new AttackTransferDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jCancelTransferButton;
    private javax.swing.JButton jDoTransferButton;
    private javax.swing.JComboBox jExistingPlanBox;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private org.jdesktop.swingx.JXTextField jNewPlanName;
    // End of variables declaration//GEN-END:variables
}
