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
import de.tor.tribes.util.AttackToTextWriter;
import de.tor.tribes.util.GlobalOptions;
import de.tor.tribes.util.JOptionPaneHelper;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;

/**
 *
 * @author Torridity
 */
public class AttacksToTextExportDialog extends javax.swing.JDialog {

    private static Logger logger = Logger.getLogger("AttacksToTextExportDialog");
    private boolean result = false;
    private Attack[] attacks = null;

    /**
     * Creates new form AttacksToTextExportDialog
     */
    public AttacksToTextExportDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        jTargetFolder = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        jUseExtendedBBCode = new javax.swing.JCheckBox();
        jXLabel3 = new org.jdesktop.swingx.JXLabel();
        jZipResult = new javax.swing.JCheckBox();
        jXLabel4 = new org.jdesktop.swingx.JXLabel();
        jAttacksPerFile = new javax.swing.JTextField();
        jOKButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Angriffe in Textdateien aufteilen");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jXLabel1.setText("Zielverzeichnis");
        jXLabel1.setMaximumSize(new java.awt.Dimension(120, 14));
        jXLabel1.setMinimumSize(new java.awt.Dimension(120, 14));
        jXLabel1.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jXLabel1, gridBagConstraints);

        jTargetFolder.setEditable(false);
        jTargetFolder.setMinimumSize(new java.awt.Dimension(120, 20));
        jTargetFolder.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTargetFolder, gridBagConstraints);

        jButton1.setText("...");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fireShowTargetSelectionEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        jXLabel2.setText("Erweiterte BB-Codes");
        jXLabel2.setMaximumSize(new java.awt.Dimension(120, 14));
        jXLabel2.setMinimumSize(new java.awt.Dimension(120, 14));
        jXLabel2.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jXLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jUseExtendedBBCode, gridBagConstraints);

        jXLabel3.setText("Einzeldateien packen");
        jXLabel3.setMaximumSize(new java.awt.Dimension(120, 14));
        jXLabel3.setMinimumSize(new java.awt.Dimension(120, 14));
        jXLabel3.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jXLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jZipResult, gridBagConstraints);

        jXLabel4.setText("Angriffe pro Datei");
        jXLabel4.setMaximumSize(new java.awt.Dimension(120, 14));
        jXLabel4.setMinimumSize(new java.awt.Dimension(120, 14));
        jXLabel4.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jXLabel4, gridBagConstraints);

        jAttacksPerFile.setText("10");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jAttacksPerFile, gridBagConstraints);

        jOKButton.setText("Speichern");
        jOKButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fireCloseEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        getContentPane().add(jOKButton, gridBagConstraints);

        jButton3.setText("Abbrechen");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fireCloseEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        getContentPane().add(jButton3, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public boolean setupAndShow(List<Attack> pAttacks) {
        attacks = pAttacks.toArray(new Attack[pAttacks.size()]);
        String dir = GlobalOptions.getProperty("screen.dir");

        jTargetFolder.setText(new File(dir).getPath());
        String val = GlobalOptions.getProperty("text.attacks.per.file");
        jAttacksPerFile.setText(val);
        jUseExtendedBBCode.setSelected(GlobalOptions.getProperties().getBoolean("extended.text.attacks"));
        jZipResult.setSelected(GlobalOptions.getProperties().getBoolean("zip.text.attacks"));

        pack();
        setVisible(true);
        return result;
    }

    private void fireShowTargetSelectionEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireShowTargetSelectionEvent

        String dir = GlobalOptions.getProperty("screen.dir");
        JFileChooser chooser = null;
        try {
            chooser = new JFileChooser(dir);
        } catch (Exception e) {
            JOptionPaneHelper.showErrorBox(this, "Konnte Dateiauswahldialog nicht öffnen.\nMöglicherweise verwendest du Windows Vista. Ist dies der Fall, beende DS Workbench, klicke mit der rechten Maustaste auf DSWorkbench.exe,\n" + "wähle 'Eigenschaften' und deaktiviere dort unter 'Kompatibilität' den Windows XP Kompatibilitätsmodus.", "Fehler");
            return;
        }

        chooser.setDialogTitle("Zielverzeichnis auswählen");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        chooser.setSelectedFile(new File(dir));
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                File f = chooser.getSelectedFile();
                jTargetFolder.setText(f.getPath());
                //store current directory
                GlobalOptions.addProperty("screen.dir", f.getPath());
            } catch (Exception e) {
                logger.error("Failed to select target path", e);
            }
        }
    }//GEN-LAST:event_fireShowTargetSelectionEvent

    private void fireCloseEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCloseEvent
        if (evt.getSource() == jOKButton) {
            File target = new File(jTargetFolder.getText());
            if (!target.exists() || !target.canWrite()) {
                logger.error("Target path '" + jTargetFolder.getText() + "' is not writeable or does not exist");
                result = false;
                return;
            }
            int attacksPerFile = 10;

            try {
                attacksPerFile = Integer.parseInt(jAttacksPerFile.getText());
            } catch (Exception e) {
                attacksPerFile = 10;
                jAttacksPerFile.setText("10");
            }
            result = AttackToTextWriter.writeAttacks(attacks, target, attacksPerFile, jUseExtendedBBCode.isSelected(), jZipResult.isSelected());

            if (result) {
                GlobalOptions.addProperty("text.attacks.per.file", jAttacksPerFile.getText());
                GlobalOptions.addProperty("extended.text.attacks", Boolean.toString(jUseExtendedBBCode.isSelected()));
                GlobalOptions.addProperty("zip.text.attacks", Boolean.toString(jZipResult.isSelected()));
            }

        }
        dispose();
    }//GEN-LAST:event_fireCloseEvent

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
            java.util.logging.Logger.getLogger(AttacksToTextExportDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                AttacksToTextExportDialog dialog = new AttacksToTextExportDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField jAttacksPerFile;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jOKButton;
    private javax.swing.JTextField jTargetFolder;
    private javax.swing.JCheckBox jUseExtendedBBCode;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private org.jdesktop.swingx.JXLabel jXLabel3;
    private org.jdesktop.swingx.JXLabel jXLabel4;
    private javax.swing.JCheckBox jZipResult;
    // End of variables declaration//GEN-END:variables
}
