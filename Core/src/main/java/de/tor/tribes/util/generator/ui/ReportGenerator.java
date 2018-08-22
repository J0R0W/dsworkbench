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
package de.tor.tribes.util.generator.ui;

import de.tor.tribes.io.DataHolder;
import de.tor.tribes.io.TroopAmountFixed;
import de.tor.tribes.types.FarmInformation;
import de.tor.tribes.types.FightReport;
import de.tor.tribes.types.ext.InvalidTribe;
import de.tor.tribes.types.ext.Tribe;
import de.tor.tribes.types.ext.Village;
import de.tor.tribes.util.PluginManager;
import de.tor.tribes.util.UIHelper;
import de.tor.tribes.util.farm.FarmManager;
import de.tor.tribes.util.report.ReportManager;
import de.tor.tribes.util.village.KnownVillage;
import de.tor.tribes.util.xml.JDomUtils;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author Torridity
 */
public class ReportGenerator extends javax.swing.JFrame {

    private static Logger logger = LogManager.getLogger("ReportGenerator");

    /**
     * Creates new form ReportGenerator
     */
    public ReportGenerator() {
        initComponents();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jFarming = new javax.swing.JRadioButton();
        jOffing = new javax.swing.JRadioButton();
        jFaking = new javax.swing.JRadioButton();
        jSnobbing = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSomeDef = new javax.swing.JRadioButton();
        jFullDef = new javax.swing.JRadioButton();
        jEmpty = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jWoodHaul = new org.jdesktop.swingx.JXTextField();
        jClayHaul = new org.jdesktop.swingx.JXTextField();
        jIronHaul = new org.jdesktop.swingx.JXTextField();
        jRemainPercent = new org.jdesktop.swingx.JXTextField();
        jWoodLevel = new org.jdesktop.swingx.JXTextField();
        jClayLevel = new org.jdesktop.swingx.JXTextField();
        jIronLevel = new org.jdesktop.swingx.JXTextField();
        jStorageLevel = new org.jdesktop.swingx.JXTextField();
        jHideLevel = new org.jdesktop.swingx.JXTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTroopsOutside = new javax.swing.JCheckBox();
        jTroopsOnTheWay = new javax.swing.JCheckBox();
        jWallDamaged = new javax.swing.JCheckBox();
        jBuildingDamaged = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jReportCount = new org.jdesktop.swingx.JXTextField();
        jAttacker = new org.jdesktop.swingx.JXTextField();
        jSource = new org.jdesktop.swingx.JXTextField();
        jDefender = new org.jdesktop.swingx.JXTextField();
        jTarget = new org.jdesktop.swingx.JXTextField();

        setTitle("Random Report Generator");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Attacker");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText("Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        buttonGroup1.add(jFarming);
        jFarming.setText("Farm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jFarming, gridBagConstraints);

        buttonGroup1.add(jOffing);
        jOffing.setSelected(true);
        jOffing.setText("Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jOffing, gridBagConstraints);

        buttonGroup1.add(jFaking);
        jFaking.setText("Fake");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jFaking, gridBagConstraints);

        buttonGroup1.add(jSnobbing);
        jSnobbing.setText("Snob");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jSnobbing, gridBagConstraints);

        jLabel3.setText("Defender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Target");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        buttonGroup2.add(jSomeDef);
        jSomeDef.setSelected(true);
        jSomeDef.setText("Some Def");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jSomeDef, gridBagConstraints);

        buttonGroup2.add(jFullDef);
        jFullDef.setText("Full Def");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jFullDef, gridBagConstraints);

        buttonGroup2.add(jEmpty);
        jEmpty.setText("Empty");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jEmpty, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Spy&Haul"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jWoodHaul.setPrompt("Wood");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jWoodHaul, gridBagConstraints);

        jClayHaul.setPrompt("Clay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jClayHaul, gridBagConstraints);

        jIronHaul.setPrompt("Iron");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jIronHaul, gridBagConstraints);

        jRemainPercent.setPrompt("% Remain");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jRemainPercent, gridBagConstraints);

        jWoodLevel.setPrompt("Wood");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jWoodLevel, gridBagConstraints);

        jClayLevel.setPrompt("Clay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jClayLevel, gridBagConstraints);

        jIronLevel.setPrompt("Iron");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jIronLevel, gridBagConstraints);

        jStorageLevel.setPrompt("Storage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jStorageLevel, gridBagConstraints);

        jHideLevel.setPrompt("Hide");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jHideLevel, gridBagConstraints);

        jLabel5.setText("Haul");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Buildings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        jButton1.setText("Create Single");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireCreateReportEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Misc Features"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jTroopsOutside.setText("Troops Outside");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTroopsOutside, gridBagConstraints);

        jTroopsOnTheWay.setText("Troops On The Way");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTroopsOnTheWay, gridBagConstraints);

        jWallDamaged.setSelected(true);
        jWallDamaged.setText("Wall Damaged");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jWallDamaged, gridBagConstraints);

        jBuildingDamaged.setSelected(true);
        jBuildingDamaged.setText("Building Damaged");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jBuildingDamaged, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel2, gridBagConstraints);

        jButton2.setText("Create Many");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fireCreateManyReportsEvent(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton2, gridBagConstraints);

        jReportCount.setPrompt("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jReportCount, gridBagConstraints);

        jAttacker.setPrompt("Attacker");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jAttacker, gridBagConstraints);

        jSource.setPrompt("Source Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jSource, gridBagConstraints);

        jDefender.setPrompt("Defender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jDefender, gridBagConstraints);

        jTarget.setPrompt("Target Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTarget, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fireCreateReportEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCreateReportEvent
        generateReport();
    }//GEN-LAST:event_fireCreateReportEvent

    private void fireCreateManyReportsEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fireCreateManyReportsEvent
        int amount = UIHelper.parseIntFromField(jReportCount, 1);
        for (int i = 0; i < amount; i++) {
            System.out.println("Generating report " + (i + 1));
            generateReport();
        }
    }//GEN-LAST:event_fireCreateManyReportsEvent

    public void generateReport() {
        FightReport r = new FightReport();
        r.setTimestamp(System.currentTimeMillis());
        Village source = getVillage(jAttacker.getText(), jSource.getText());
        Village target = getVillage(jDefender.getText(), jTarget.getText());

        r.setSourceVillage(source);
        r.setTargetVillage(target);
        r.setAttacker(source.getTribe());
        r.setDefender(target.getTribe());
        r.setAttackers(getAttackingTroops(r.getTargetVillage()));
        r.setDefenders(getDefendingTroops());
        r.setDiedAttackers((jFullDef.isSelected()) ? getReducedTroopAmount(r.getAttackers(), 0.0)
                : (jSomeDef.isSelected()) ? getReducedTroopAmount(r.getAttackers(), Math.max(.3, Math.random()))
                : getReducedTroopAmount(r.getAttackers(), 1.0));
        r.setDiedDefenders((jFullDef.isSelected()) ? getReducedTroopAmount(r.getDefenders(), Math.max(.2, Math.random()))
                : (jSomeDef.isSelected()) ? getReducedTroopAmount(r.getDefenders(), 0.0)
                : getReducedTroopAmount(r.getDefenders(), 0.0));

        if (!jFullDef.isSelected() && jSnobbing.isSelected()) {
            r.setConquered(true);
            r.setAcceptanceBefore((byte) 10);
            r.setAcceptanceAfter((byte) -10);
        }
        if (hasHaul()) {
            r.setHaul(UIHelper.parseIntFromField(jWoodHaul), UIHelper.parseIntFromField(jClayHaul), UIHelper.parseIntFromField(jIronHaul));
        }

        int remain = UIHelper.parseIntFromField(jRemainPercent);
        r.setSpyedResources((int) Math.rint(remain / 100.0 * (double) r.getHaul()[0]), (int) Math.rint(remain / 100.0 * (double) r.getHaul()[1]), (int) Math.rint(remain / 100.0 * (double) r.getHaul()[2]));

        int[] spyInfo = getSpyInfo();
        if (spyInfo != null) {
            r.setBuilding(KnownVillage.getBuildingIdByName("timber"), spyInfo[0]);
            r.setBuilding(KnownVillage.getBuildingIdByName("clay"), spyInfo[1]);
            r.setBuilding(KnownVillage.getBuildingIdByName("iron"), spyInfo[2]);
            r.setBuilding(KnownVillage.getBuildingIdByName("storage"), spyInfo[3]);
            r.setBuilding(KnownVillage.getBuildingIdByName("hide"), spyInfo[4]);
        }

        if (jTroopsOutside.isSelected()) {
            r.addDefendersOutside(DataHolder.getSingleton().getRandomVillage(), getReducedTroopAmount(getDefendingTroops(), Math.max(.2, Math.random())));
            r.addDefendersOutside(DataHolder.getSingleton().getRandomVillage(), getDefendingTroops());
        }

        if (jTroopsOnTheWay.isSelected()) {
            r.setDefendersOnTheWay(getReducedTroopAmount(getDefendingTroops(), Math.max(.2, Math.random())));
        }
        if (jWallDamaged.isSelected()) {
            r.setWallBefore((byte) 20);
            r.setWallAfter((byte) 10);
        }

        if (jBuildingDamaged.isSelected()) {
            r.setBuildingBefore((byte) 20);
            r.setBuildingAfter((byte) 10);
            r.setAimedBuildingId(KnownVillage.getBuildingIdByName("farm"));
        }

        System.out.println("---Adding generated report (Valid:" + r.isValid() + ")----");
        System.out.println(JDomUtils.toShortString(r.toXml("report")));
        ReportManager.getSingleton().addManagedElement(r);
        System.out.println("----Done----");
    }

    private Village getVillage(String pTribeString, String pVillageString) {
        Village result;
        List<Village> target = PluginManager.getSingleton().executeVillageParser(pVillageString);
        if (target.isEmpty()) {
            Tribe t = DataHolder.getSingleton().getTribeByName(pTribeString);
            if (!t.equals(InvalidTribe.getSingleton())) {
                result = t.getVillageList()[(int) (Math.random() * t.getVillages() - 1)];
            } else {
                result = DataHolder.getSingleton().getRandomVillage();
            }
        } else {
            result = target.get(0);
        }

        return result;
    }

    private TroopAmountFixed getAttackingTroops(Village pVillage) {
        TroopAmountFixed units = new TroopAmountFixed(0);
        if (jFarming.isSelected()) {
            FarmInformation info = FarmManager.getSingleton().getFarmInformation(pVillage);
            if (info == null || info.getFarmTroop() == null) {
                units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("light"), getRandomValueInRange(30, 60));
                units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("spy"), 1);
            } else {
                units = info.getFarmTroop();
            }
        } else if (jOffing.isSelected()) {
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("axe"), getRandomValueInRange(5000, 7000));
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("light"), getRandomValueInRange(2000, 2300));
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("spy"), getRandomValueInRange(100, 150));
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("ram"), getRandomValueInRange(240, 300));
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("catapult"), getRandomValueInRange(20, 50));
        } else if (jFaking.isSelected()) {
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("spear"), getRandomValueInRange(80, 110));
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("spy"), getRandomValueInRange(20, 40));
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("ram"), 1);
        } else if (jSnobbing.isSelected()) {
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("axe"), getRandomValueInRange(100, 300));
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("spy"), 1);
            units.setAmountForUnit(DataHolder.getSingleton().getUnitByPlainName("snob"), 1);
        }
        return units;
    }

    public TroopAmountFixed getReducedTroopAmount(TroopAmountFixed pUnits, double pPercent) {
        TroopAmountFixed result = pUnits.clone();
        result.multiplyWith(1d - pPercent);
        return result;
    }

    private TroopAmountFixed getDefendingTroops() {
       TroopAmountFixed units = new TroopAmountFixed(0);
        if (jSomeDef.isSelected()) {
            units.setAmountForUnit("spear", getRandomValueInRange(1000, 2000));
            units.setAmountForUnit("sword", getRandomValueInRange(1000, 2000));
            units.setAmountForUnit("heavy", getRandomValueInRange(300, 500));
            units.setAmountForUnit("spy", getRandomValueInRange(100, 200));
        } else if (jFullDef.isSelected()) {
            units.fill(-1);
        } else if (jEmpty.isSelected()) {
            //add nothing
        }
        
        return units;
    }

    private int getRandomValueInRange(int min, int max) {
        return Math.max(min, (int) (Math.random() * max));
    }

    private boolean hasHaul() {
        return UIHelper.parseIntFromField(jWoodHaul) != 0 || UIHelper.parseIntFromField(jClayHaul) != 0 || UIHelper.parseIntFromField(jIronHaul) != 0;
    }

    private int[] getSpyInfo() {

        /*
         * if (ArrayUtils.contains(result, 0)) {//all buildings or nothing
         * return null; }
         */
        return new int[]{
            UIHelper.parseIntFromField(jWoodLevel),
            UIHelper.parseIntFromField(jClayLevel),
            UIHelper.parseIntFromField(jIronLevel),
            UIHelper.parseIntFromField(jStorageLevel),
            UIHelper.parseIntFromField(jHideLevel)};
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private org.jdesktop.swingx.JXTextField jAttacker;
    private javax.swing.JCheckBox jBuildingDamaged;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private org.jdesktop.swingx.JXTextField jClayHaul;
    private org.jdesktop.swingx.JXTextField jClayLevel;
    private org.jdesktop.swingx.JXTextField jDefender;
    private javax.swing.JRadioButton jEmpty;
    private javax.swing.JRadioButton jFaking;
    private javax.swing.JRadioButton jFarming;
    private javax.swing.JRadioButton jFullDef;
    private org.jdesktop.swingx.JXTextField jHideLevel;
    private org.jdesktop.swingx.JXTextField jIronHaul;
    private org.jdesktop.swingx.JXTextField jIronLevel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton jOffing;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private org.jdesktop.swingx.JXTextField jRemainPercent;
    private org.jdesktop.swingx.JXTextField jReportCount;
    private javax.swing.JRadioButton jSnobbing;
    private javax.swing.JRadioButton jSomeDef;
    private org.jdesktop.swingx.JXTextField jSource;
    private org.jdesktop.swingx.JXTextField jStorageLevel;
    private org.jdesktop.swingx.JXTextField jTarget;
    private javax.swing.JCheckBox jTroopsOnTheWay;
    private javax.swing.JCheckBox jTroopsOutside;
    private javax.swing.JCheckBox jWallDamaged;
    private org.jdesktop.swingx.JXTextField jWoodHaul;
    private org.jdesktop.swingx.JXTextField jWoodLevel;
    // End of variables declaration//GEN-END:variables
}
