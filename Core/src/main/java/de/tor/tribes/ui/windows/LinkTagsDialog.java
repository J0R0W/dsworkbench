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

import de.tor.tribes.control.ManageableType;
import de.tor.tribes.io.DataHolder;
import de.tor.tribes.types.LinkedTag;
import de.tor.tribes.types.Tag;
import de.tor.tribes.ui.editors.LinkGroupColorCellEditor;
import de.tor.tribes.ui.models.TagLinkMatrixModel;
import de.tor.tribes.ui.renderer.AlternatingColorCellRenderer;
import de.tor.tribes.ui.renderer.DefaultTableHeaderRenderer;
import de.tor.tribes.ui.renderer.MultiColorCellRenderer;
import de.tor.tribes.util.Constants;
import de.tor.tribes.util.GlobalOptions;
import de.tor.tribes.util.JOptionPaneHelper;
import de.tor.tribes.util.ProfileManager;
import de.tor.tribes.util.tag.TagManager;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.UIManager;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author Torridity
 */
public class LinkTagsDialog extends javax.swing.JDialog {

    private boolean bCreateLinkedTag = false;

    /** Creates new form LinkTagDialog */
    public LinkTagsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public LinkedTag setupAndShow() {
        jXTable1.setModel(new TagLinkMatrixModel());
        HighlightPredicate.ColumnHighlightPredicate colu = new HighlightPredicate.ColumnHighlightPredicate(0);
        jXTable1.setHighlighters(new CompoundHighlighter(colu, HighlighterFactory.createAlternateStriping(Constants.DS_ROW_A, Constants.DS_ROW_B)));
        jXTable1.setColumnControlVisible(false);
        jXTable1.setDefaultRenderer(Integer.class, new MultiColorCellRenderer());
        jXTable1.setDefaultRenderer(Tag.class, new AlternatingColorCellRenderer());
        jXTable1.setDefaultEditor(Integer.class, new LinkGroupColorCellEditor());
        jXTable1.setRowHeight(21);
        jXTable1.getTableHeader().setDefaultRenderer(new DefaultTableHeaderRenderer());
        setVisible(true);

        if (bCreateLinkedTag) {
            LinkedTag t = new LinkedTag(jTagName.getText(), true);
            String equation = ((TagLinkMatrixModel) jXTable1.getModel()).getEquation();
            equation = equation.replaceAll("UND", "&&");
            equation = equation.replaceAll("ODER", "||");
            t.setEquation(equation);
            t.updateVillageList();
            return t;
        }
        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jDoCreateButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTagName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jXTable1 = new org.jdesktop.swingx.JXTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gruppen kombinieren");
        setAlwaysOnTop(true);

        jPanel1.setBackground(new java.awt.Color(239, 235, 223));

        jDoCreateButton.setText("Erstellen");
        jDoCreateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireAcceptEvent(evt);
            }
        });

        jButton2.setText("Abbrechen");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireAcceptEvent(evt);
            }
        });

        jLabel1.setText("Name der neuen Gruppe");

        jTagName.setMinimumSize(new java.awt.Dimension(100, 20));
        jTagName.setPreferredSize(new java.awt.Dimension(100, 20));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ui/information.png"))); // NOI18N
        jButton1.setText("Verknüpfung als Klartext");
        jButton1.setToolTipText("Zeigt die resultierende Verknüpfung in Textform mit farblicher Abgrenzung");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireShowLinkInPlainTextEvent(evt);
            }
        });

        jXTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jXTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTagName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(432, 432, 432))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 348, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDoCreateButton)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTagName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDoCreateButton)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fireAcceptEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireAcceptEvent
        if (evt.getSource() == jDoCreateButton) {
            if (jTagName.getText().length() < 1) {
                JOptionPaneHelper.showWarningBox(this, "Du musst einen Namen für den neuen Gruppe angeben.", "Warnung");
                return;
            }
            if (jTagName.getText().equals("ODER") || jTagName.getText().equals("UND")) {
                JOptionPaneHelper.showWarningBox(this, "Folgende Begriffe sind als Gruppennamen gesperrt: UND, ODER\nGib bitte einen anderen Namen an.", "Warnung");
                return;
            }

            String equation = ((TagLinkMatrixModel) jXTable1.getModel()).getEquation();
            List<ManageableType> elements = TagManager.getSingleton().getAllElements();
            List<Tag> lTags = new LinkedList<Tag>();
            for (ManageableType e : elements) {
                lTags.add((Tag) e);
            }
            Collections.sort(lTags, Tag.SIZE_ORDER);

            for (Tag t : lTags) {
                //for (Tag t : pTags) {
                equation = equation.replaceAll(Matcher.quoteReplacement(t.getName()), "true");
            }

            for (int i = 0; i < 99; i++) {
                equation = equation.replaceAll(("K" + ((i < 10) ? "0" : "") + i), "true");
            }

            equation = equation.replaceAll("UND", "&&");
            equation = equation.replaceAll("ODER", "||");
            ScriptEngineManager factory = new ScriptEngineManager();
            // create a JavaScript engine
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            // evaluate JavaScript code from String
            try {
                engine.eval("var b = eval(\"" + equation + "\")");
            } catch (Exception e) {
                JOptionPaneHelper.showWarningBox(this, "Die angegebene Verknüpfung scheint fehlerhaft zu sein.\nBitte überprüfe sie noch einmal.", "Warnung");
                return;
            }

            bCreateLinkedTag = true;
        }
        dispose();
    }//GEN-LAST:event_fireAcceptEvent

    private void fireShowLinkInPlainTextEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireShowLinkInPlainTextEvent
        String equation = ((TagLinkMatrixModel) jXTable1.getModel()).getEquationAsHtml();
        
        String name = jTagName.getText();
        if(name.length() == 0){
            name = "Kein Name";
        }
        JOptionPaneHelper.showInformationBox(this, "<html>Die Dörfer der verknüpften Gruppe mit dem Namen '" + name + "' befinden sich<BR/> " + equation + "</html>", "Verknüpfung");
    }//GEN-LAST:event_fireShowLinkInPlainTextEvent

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
        Logger.getRootLogger().addAppender(new ConsoleAppender(new org.apache.log4j.PatternLayout("%d - %-5p - %-20c (%C [%L]) - %m%n")));
        GlobalOptions.setSelectedServer("de43");
        DataHolder.getSingleton().loadData(false);
        ProfileManager.getSingleton().loadProfiles();
        TagManager.getSingleton().addTag("Test");

        GlobalOptions.setSelectedProfile(ProfileManager.getSingleton().getProfiles("de43")[0]);

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                LinkTagsDialog dialog = new LinkTagsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setupAndShow();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jDoCreateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTagName;
    private org.jdesktop.swingx.JXTable jXTable1;
    // End of variables declaration//GEN-END:variables
}
