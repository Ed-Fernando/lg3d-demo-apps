/**
 * Project Looking Glass
 *
 * $RCSfile: ControlFrame.java,v $
 *
 * Copyright (c) 2004, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.1 $
 * $Date: 2006-04-04 00:47:11 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.apps.swingnode;

import javax.vecmath.Vector3f;

/**
 *
 * @author  paulby
 */
public class ControlFrame extends javax.swing.JFrame {
    
    private ClothTest clothTest;
    
    /** Creates new form ControlFrame */
    public ControlFrame(ClothTest clothTest) {
        this.clothTest = clothTest;
        initComponents();
        springTensionTF.setText(Float.toString(clothTest.getSpringTensionConstant()));
        springShearTF.setText(Float.toString(clothTest.getSpringShearConstant()));
        springDampingTF.setText(Float.toString(clothTest.getSpringDampingConstant()));
        windFactorTF.setText(Float.toString(clothTest.getWindFactor()));
        Vector3f windVector = clothTest.getWindVector();
        windVectorXTF.setText(Float.toString(windVector.x));
        windVectorYTF.setText(Float.toString(windVector.y));
        windVectorZTF.setText(Float.toString(windVector.z));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        stepB = new javax.swing.JButton();
        singleStepCB = new javax.swing.JCheckBox();
        resetGeomB = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        springTensionTF = new javax.swing.JTextField();
        springShearTF = new javax.swing.JTextField();
        springDampingTF = new javax.swing.JTextField();
        physicsApplyB = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        windFactorTF = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        windVectorXTF = new javax.swing.JTextField();
        windVectorYTF = new javax.swing.JTextField();
        windVectorZTF = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Controls");
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        stepB.setText("Step");
        stepB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepBActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(stepB, gridBagConstraints);

        singleStepCB.setText("Single Step");
        singleStepCB.setToolTipText("Single step simulation using Step button");
        singleStepCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        singleStepCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        singleStepCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleStepCBActionPerformed(evt);
            }
        });

        jPanel1.add(singleStepCB, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel2.add(jPanel1, gridBagConstraints);

        resetGeomB.setText("Reset Geometry");
        resetGeomB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetGeomBActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel2.add(resetGeomB, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Simulation Variables"));
        jLabel2.setText("Spring Tension");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Spring Shear");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel3.add(jLabel3, gridBagConstraints);

        jLabel1.setText("Spring Damping");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel3.add(jLabel1, gridBagConstraints);

        springTensionTF.setText("jTextField3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(springTensionTF, gridBagConstraints);

        springShearTF.setText("jTextField2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(springShearTF, gridBagConstraints);

        springDampingTF.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(springDampingTF, gridBagConstraints);

        physicsApplyB.setText("Apply");
        physicsApplyB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                physicsApplyBActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel3.add(physicsApplyB, gridBagConstraints);

        jLabel4.setText("Wind Factor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jLabel4, gridBagConstraints);

        windFactorTF.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(windFactorTF, gridBagConstraints);

        jLabel5.setText("Wind Vector");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jLabel5, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        windVectorXTF.setColumns(3);
        windVectorXTF.setText("jTextField4");
        jPanel4.add(windVectorXTF, new java.awt.GridBagConstraints());

        windVectorYTF.setColumns(3);
        windVectorYTF.setText("jTextField3");
        jPanel4.add(windVectorYTF, new java.awt.GridBagConstraints());

        windVectorZTF.setColumns(3);
        windVectorZTF.setText("jTextField2");
        jPanel4.add(windVectorZTF, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanel3.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel2.add(jPanel3, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void physicsApplyBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_physicsApplyBActionPerformed
// TODO add your handling code here:
        try {
            clothTest.updateSpringConstants(Float.valueOf(springTensionTF.getText()),
                    Float.valueOf(springShearTF.getText()),
                    Float.valueOf(springDampingTF.getText()));
            clothTest.setWindFactor(Float.valueOf(windFactorTF.getText()));
         } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_physicsApplyBActionPerformed

    private void resetGeomBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetGeomBActionPerformed
// TODO add your handling code here:
        clothTest.resetGeom();
    }//GEN-LAST:event_resetGeomBActionPerformed

    private void stepBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepBActionPerformed
// TODO add your handling code here:
        clothTest.step();
    }//GEN-LAST:event_stepBActionPerformed

    private void singleStepCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleStepCBActionPerformed
// TODO add your handling code here:
        clothTest.setSingleStep(singleStepCB.isSelected());
    }//GEN-LAST:event_singleStepCBActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton physicsApplyB;
    private javax.swing.JButton resetGeomB;
    private javax.swing.JCheckBox singleStepCB;
    private javax.swing.JTextField springDampingTF;
    private javax.swing.JTextField springShearTF;
    private javax.swing.JTextField springTensionTF;
    private javax.swing.JButton stepB;
    private javax.swing.JTextField windFactorTF;
    private javax.swing.JTextField windVectorXTF;
    private javax.swing.JTextField windVectorYTF;
    private javax.swing.JTextField windVectorZTF;
    // End of variables declaration//GEN-END:variables
    
}
