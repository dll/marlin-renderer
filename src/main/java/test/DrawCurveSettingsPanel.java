/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package test;

/**
 *
 */
final class DrawCurveSettingsPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    // members:
    private DrawCurveApplication app = null;

    /** Creates new form DrawCurveSettingsPanel */
    DrawCurveSettingsPanel() {
        initComponents();
    }

    private void updateForm() {
        if (app == null) {
            return;
        }
        final DrawCurveApplication a = this.app;
        try {
            // set to null to disable event loop:
            this.app = null;

            // stroke parameters:
            jSliderStrokeWidthSlider.setValue((int) (a.strokeWidth * 100f));
            jSliderStrokeWidthSliderStateChanged(null);
            jComboBoxCap.setSelectedIndex(a.strokeCap);
            jComboBoxJoin.setSelectedIndex(a.strokeJoin);
            jToggleButtonUseDashes.setSelected(a.useDashes);
            jToggleButtonUseDashesActionPerformed(null);
            /*
                TODO: edit dashes array
             */

            // shape parameters:
            jToggleButtonShowQuad.setSelected(a.showQuad.get());
            jToggleButtonShowQuadActionPerformed(null);
            jToggleButtonShowCubic.setSelected(a.showCubic.get());
            jToggleButtonShowCubicActionPerformed(null);
            jToggleButtonShowEllipse.setSelected(a.showEllipse);
            jToggleButtonShowEllipseActionPerformed(null);

            // painting parameters:
            jToggleButtonShowExtra.setSelected(a.showExtra);
            jToggleButtonShowExtraActionPerformed(null);
            /*
                TODO: other settings
                boolean showOutline = false;
                boolean paintControls = true;
                boolean paintDetails = true;
             */
        } finally {
            // restore app:
            this.app = a;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelStroke = new javax.swing.JPanel();
        jLabelStrokeWidth = new javax.swing.JLabel();
        jSliderStrokeWidthSlider = new javax.swing.JSlider();
        jLabelStrokeCap = new javax.swing.JLabel();
        jComboBoxCap = new javax.swing.JComboBox();
        jLabelStrokeJoin = new javax.swing.JLabel();
        jComboBoxJoin = new javax.swing.JComboBox();
        jLabelStrokeUseDashes = new javax.swing.JLabel();
        jToggleButtonUseDashes = new javax.swing.JToggleButton();
        jPanelShape = new javax.swing.JPanel();
        jLabelStrokeShowQuad = new javax.swing.JLabel();
        jToggleButtonShowQuad = new javax.swing.JToggleButton();
        jLabelStrokeShowCubic = new javax.swing.JLabel();
        jToggleButtonShowCubic = new javax.swing.JToggleButton();
        jLabelStrokeShowEllipse = new javax.swing.JLabel();
        jToggleButtonShowEllipse = new javax.swing.JToggleButton();
        jPanelPaint = new javax.swing.JPanel();
        jLabelStrokeShowExtra = new javax.swing.JLabel();
        jToggleButtonShowExtra = new javax.swing.JToggleButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        jPanelStroke.setBorder(javax.swing.BorderFactory.createTitledBorder("Stroke"));
        jPanelStroke.setName("jPanelStroke"); // NOI18N
        jPanelStroke.setLayout(new java.awt.GridBagLayout());

        jLabelStrokeWidth.setText("Width:");
        jLabelStrokeWidth.setName("jLabelStrokeWidth"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStroke.add(jLabelStrokeWidth, gridBagConstraints);

        jSliderStrokeWidthSlider.setMajorTickSpacing(10000);
        jSliderStrokeWidthSlider.setMaximum(100000);
        jSliderStrokeWidthSlider.setPaintTicks(true);
        jSliderStrokeWidthSlider.setValue(40000);
        jSliderStrokeWidthSlider.setName("jSliderStrokeWidthSlider"); // NOI18N
        jSliderStrokeWidthSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderStrokeWidthSliderStateChanged(evt);
            }
        });
        jPanelStroke.add(jSliderStrokeWidthSlider, new java.awt.GridBagConstraints());

        jLabelStrokeCap.setText("Cap:");
        jLabelStrokeCap.setName("jLabelStrokeCap"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStroke.add(jLabelStrokeCap, gridBagConstraints);

        jComboBoxCap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BUTTON", "ROUND", "SQUARE" }));
        jComboBoxCap.setName("jComboBoxCap"); // NOI18N
        jComboBoxCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCapActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStroke.add(jComboBoxCap, gridBagConstraints);

        jLabelStrokeJoin.setText("Join:");
        jLabelStrokeJoin.setName("jLabelStrokeJoin"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStroke.add(jLabelStrokeJoin, gridBagConstraints);

        jComboBoxJoin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MITER", "ROUND", "BEVEL" }));
        jComboBoxJoin.setName("jComboBoxJoin"); // NOI18N
        jComboBoxJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxJoinActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStroke.add(jComboBoxJoin, gridBagConstraints);

        jLabelStrokeUseDashes.setText("Dashes:");
        jLabelStrokeUseDashes.setName("jLabelStrokeUseDashes"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStroke.add(jLabelStrokeUseDashes, gridBagConstraints);

        jToggleButtonUseDashes.setText("Enabled");
        jToggleButtonUseDashes.setName("jToggleButtonUseDashes"); // NOI18N
        jToggleButtonUseDashes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonUseDashesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanelStroke.add(jToggleButtonUseDashes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanelStroke, gridBagConstraints);

        jPanelShape.setBorder(javax.swing.BorderFactory.createTitledBorder("Shapes"));
        jPanelShape.setName("jPanelShape"); // NOI18N
        jPanelShape.setLayout(new java.awt.GridBagLayout());

        jLabelStrokeShowQuad.setText("Quadratic curve:");
        jLabelStrokeShowQuad.setName("jLabelStrokeShowQuad"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelShape.add(jLabelStrokeShowQuad, gridBagConstraints);

        jToggleButtonShowQuad.setText("ENABLED");
        jToggleButtonShowQuad.setName("jToggleButtonShowQuad"); // NOI18N
        jToggleButtonShowQuad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonShowQuadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelShape.add(jToggleButtonShowQuad, gridBagConstraints);

        jLabelStrokeShowCubic.setText("Cubic curve:");
        jLabelStrokeShowCubic.setName("jLabelStrokeShowCubic"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelShape.add(jLabelStrokeShowCubic, gridBagConstraints);

        jToggleButtonShowCubic.setText("ENABLED");
        jToggleButtonShowCubic.setName("jToggleButtonShowCubic"); // NOI18N
        jToggleButtonShowCubic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonShowCubicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelShape.add(jToggleButtonShowCubic, gridBagConstraints);

        jLabelStrokeShowEllipse.setText("Ellipse:");
        jLabelStrokeShowEllipse.setName("jLabelStrokeShowEllipse"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelShape.add(jLabelStrokeShowEllipse, gridBagConstraints);

        jToggleButtonShowEllipse.setText("ENABLED");
        jToggleButtonShowEllipse.setName("jToggleButtonShowEllipse"); // NOI18N
        jToggleButtonShowEllipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonShowEllipseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanelShape.add(jToggleButtonShowEllipse, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelShape, gridBagConstraints);

        jPanelPaint.setBorder(javax.swing.BorderFactory.createTitledBorder("Paint settings"));
        jPanelPaint.setName("jPanelPaint"); // NOI18N
        jPanelPaint.setLayout(new java.awt.GridBagLayout());

        jLabelStrokeShowExtra.setText("Show Extra:");
        jLabelStrokeShowExtra.setName("jLabelStrokeShowExtra"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelPaint.add(jLabelStrokeShowExtra, gridBagConstraints);

        jToggleButtonShowExtra.setText("ENABLED");
        jToggleButtonShowExtra.setName("jToggleButtonShowExtra"); // NOI18N
        jToggleButtonShowExtra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonShowExtraActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelPaint.add(jToggleButtonShowExtra, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelPaint, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCapActionPerformed
        System.out.println("DrawCurveSettingsPanel: cap: " + jComboBoxCap.getSelectedIndex() + " = " + jComboBoxCap.getSelectedItem());
        if (app != null) {
            app.strokeCap = jComboBoxCap.getSelectedIndex();
            app.refresh();
        }
    }//GEN-LAST:event_jComboBoxCapActionPerformed

    private void jComboBoxJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxJoinActionPerformed
        System.out.println("DrawCurveSettingsPanel: join: " + jComboBoxJoin.getSelectedIndex() + " = " + jComboBoxJoin.getSelectedItem());
        if (app != null) {
            app.strokeJoin = jComboBoxJoin.getSelectedIndex();
            app.refresh();
        }
    }//GEN-LAST:event_jComboBoxJoinActionPerformed

    private void jToggleButtonUseDashesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonUseDashesActionPerformed
        System.out.println("DrawCurveSettingsPanel: use dashes: " + jToggleButtonUseDashes.isSelected());
        jToggleButtonUseDashes.setText((jToggleButtonUseDashes.isSelected()) ? "ENABLED" : "DISABLED");
        if (app != null) {
            app.useDashes = jToggleButtonUseDashes.isSelected();
            app.refresh();
        }
    }//GEN-LAST:event_jToggleButtonUseDashesActionPerformed

    private void jSliderStrokeWidthSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderStrokeWidthSliderStateChanged
        float width = jSliderStrokeWidthSlider.getValue() / 100f;
        System.out.println("DrawCurveSettingsPanel: width: " + width);
        if (app != null) {
            app.strokeWidth = width;
            app.refresh();
        }
    }//GEN-LAST:event_jSliderStrokeWidthSliderStateChanged

    private void jToggleButtonShowQuadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonShowQuadActionPerformed
        System.out.println("DrawCurveSettingsPanel: show quad: " + jToggleButtonShowQuad.isSelected());
        jToggleButtonShowQuad.setText((jToggleButtonShowQuad.isSelected()) ? "ENABLED" : "DISABLED");
        if (app != null) {
            app.showQuad.set(jToggleButtonShowQuad.isSelected());
            app.refresh();
        }
    }//GEN-LAST:event_jToggleButtonShowQuadActionPerformed

    private void jToggleButtonShowExtraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonShowExtraActionPerformed
        System.out.println("DrawCurveSettingsPanel: show extra: " + jToggleButtonShowExtra.isSelected());
        jToggleButtonShowExtra.setText((jToggleButtonShowExtra.isSelected()) ? "ENABLED" : "DISABLED");
        if (app != null) {
            app.showExtra = jToggleButtonShowExtra.isSelected();
            app.refresh();
        }
    }//GEN-LAST:event_jToggleButtonShowExtraActionPerformed

    private void jToggleButtonShowCubicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonShowCubicActionPerformed
        System.out.println("DrawCurveSettingsPanel: show cubic: " + jToggleButtonShowCubic.isSelected());
        jToggleButtonShowCubic.setText((jToggleButtonShowCubic.isSelected()) ? "ENABLED" : "DISABLED");
        if (app != null) {
            app.showCubic.set(jToggleButtonShowCubic.isSelected());
            app.refresh();
        }
    }//GEN-LAST:event_jToggleButtonShowCubicActionPerformed

    private void jToggleButtonShowEllipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonShowEllipseActionPerformed
        System.out.println("DrawCurveSettingsPanel: show ellipse: " + jToggleButtonShowEllipse.isSelected());
        jToggleButtonShowEllipse.setText((jToggleButtonShowEllipse.isSelected()) ? "ENABLED" : "DISABLED");
        if (app != null) {
            app.showEllipse = jToggleButtonShowEllipse.isSelected();
            app.refresh();
        }
    }//GEN-LAST:event_jToggleButtonShowEllipseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxCap;
    private javax.swing.JComboBox jComboBoxJoin;
    private javax.swing.JLabel jLabelStrokeCap;
    private javax.swing.JLabel jLabelStrokeJoin;
    private javax.swing.JLabel jLabelStrokeShowCubic;
    private javax.swing.JLabel jLabelStrokeShowEllipse;
    private javax.swing.JLabel jLabelStrokeShowExtra;
    private javax.swing.JLabel jLabelStrokeShowQuad;
    private javax.swing.JLabel jLabelStrokeUseDashes;
    private javax.swing.JLabel jLabelStrokeWidth;
    private javax.swing.JPanel jPanelPaint;
    private javax.swing.JPanel jPanelShape;
    private javax.swing.JPanel jPanelStroke;
    private javax.swing.JSlider jSliderStrokeWidthSlider;
    private javax.swing.JToggleButton jToggleButtonShowCubic;
    private javax.swing.JToggleButton jToggleButtonShowEllipse;
    private javax.swing.JToggleButton jToggleButtonShowExtra;
    private javax.swing.JToggleButton jToggleButtonShowQuad;
    private javax.swing.JToggleButton jToggleButtonUseDashes;
    // End of variables declaration//GEN-END:variables

    public DrawCurveApplication getApp() {
        return app;
    }

    public void setApp(final DrawCurveApplication app) {
        this.app = app;
        updateForm();
    }

}
