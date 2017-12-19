/*
 * FreeRails
 * Copyright (C) 2000-2018 The FreeRails Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * TrainListJPanel.java
 *
 * Created on 18 February 2004, 23:13
 */

package freerails.client.view;

import freerails.client.renderer.RenderersRoot;
import freerails.controller.ModelRoot;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.top.KEY;
import freerails.world.top.NonNullElements;
import freerails.world.top.ReadOnlyWorld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * JPanel that displays a list of trains, used for the train list window and the
 * train roaster tab.
 *
 */
public class TrainListJPanel extends javax.swing.JPanel implements View {

    private static final long serialVersionUID = 3832905463863064626L;

    private ReadOnlyWorld world;

    private FreerailsPrincipal principal;

    private int lastNumberOfTrains = -1;

    private boolean rhsjTabPane = false; // if the train list is for the

    // rhsjTabPane then use the original
    // renderer, if not use the
    // trainsummaryjpanel
    private ActionListener showTrainDetails = new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {

        }
    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeJButton;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton showDetails;
    private freerails.client.view.TrainSummaryJPanel trainSummaryJPanel1;
    private int trainViewHeight = 50;

    /**
     * Creates new form TrainListJPanel.
     */
    public TrainListJPanel() {
        initComponents();

    }

    public TrainListJPanel(boolean isInRHSJTabPane) {
        this();
        rhsjTabPane = isInRHSJTabPane;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        trainSummaryJPanel1 = new freerails.client.view.TrainSummaryJPanel();
        closeJButton = new javax.swing.JButton();
        showDetails = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        JLabel trainNumLabel = new JLabel();
        JLabel trainHeadingLabel = new JLabel();
        JLabel maintenanceLabel = new JLabel();
        JLabel incomeLabel = new JLabel();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(510, 300));
        closeJButton.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(closeJButton, gridBagConstraints);

        showDetails.setText("Show details");
        showDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDetailsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(showDetails, gridBagConstraints);

        jList1
                .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setCellRenderer(trainSummaryJPanel1);
        jList1.setDoubleBuffered(true);
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList1KeyPressed(evt);
            }
        });
        jList1
                .addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        jList1ValueChanged(evt);
                    }
                });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        trainNumLabel.setText("Train Number");
        trainNumLabel.setMaximumSize(new java.awt.Dimension(500, 500));
        trainNumLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(trainNumLabel, gridBagConstraints);

        trainHeadingLabel.setText("Headed For");
        trainHeadingLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(trainHeadingLabel, gridBagConstraints);

        maintenanceLabel.setText("Maintenance YTD");
        maintenanceLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(maintenanceLabel, gridBagConstraints);

        incomeLabel.setText("Income YTD");
        incomeLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        add(incomeLabel, new java.awt.GridBagConstraints());

    }// GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jList1ValueChanged
        // if a train is selected, enable the 'show details' button.
        if (jList1.getSelectedIndex() != -1) {
            this.showDetails.setEnabled(true);
        } else {
            this.showDetails.setEnabled(false);
        }
    }// GEN-LAST:event_jList1ValueChanged

    private void showDetailsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_showDetailsActionPerformed
        showTrainDetails.actionPerformed(evt);
    }// GEN-LAST:event_showDetailsActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jList1MouseClicked
        // Add your handling code here:
        if (evt.getClickCount() == 2) {
            showTrainDetails.actionPerformed(null);
        }
    }// GEN-LAST:event_jList1MouseClicked

    private void jList1KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jList1KeyPressed
        // Add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            showTrainDetails.actionPerformed(null);
        }
    }// GEN-LAST:event_jList1KeyPressed

    public void setup(ModelRoot mr, RenderersRoot vl, Action closeAction) {
        world = mr.getWorld();
        trainSummaryJPanel1.setup(mr, vl, null);

        if (rhsjTabPane) {
            jList1.setModel(new WorldToListModelAdapter(mr.getWorld(),
                    KEY.TRAINS, mr.getPrincipal()));
            TrainListCellRenderer trainView = new TrainListCellRenderer(mr, vl);
            jList1.setCellRenderer(trainView);
            trainView.setHeight(trainViewHeight);
        }

        ActionListener[] oldListeners = closeJButton.getActionListeners();
        for (ActionListener oldListener : oldListeners) {
            closeJButton.removeActionListener(oldListener);
        }
        closeJButton.addActionListener(closeAction);
        principal = mr.getPrincipal();
    }

    void setShowTrainDetailsActionListener(ActionListener l) {
        showTrainDetails = l;

    }

    int getSelectedTrainID() {
        /*
         * Note, the selected index is not the train id since trains that have
         * been removed are not shown on the list.
         */
        int row = jList1.getSelectedIndex();
        return NonNullElements.row2index(world, KEY.TRAINS, principal, row);
    }

    // End of variables declaration//GEN-END:variables

    /**
     * When the train list is shown on a tab we don't want the buttons.
     */
    void removeButtons() {
        this.removeAll();

        java.awt.GridBagConstraints gridBagConstraints;
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

    }

    @Override
    public void setVisible(boolean aFlag) {
        if (aFlag && null != world) {
            // jList1.setModel(new WorldToListModelAdapter(world,
            // KEY.TRAINS,principal));
        }
        super.setVisible(aFlag);
    }

    /**
     *
     * @param trainViewHeight
     */
    public void setTrainViewHeight(int trainViewHeight) {
        this.trainViewHeight = trainViewHeight;
    }

    @Override
    public void paint(Graphics g) {
        if (null != world) {
            NonNullElements trains = new NonNullElements(KEY.TRAINS, world,
                    principal);
            int newNumberOfTrains = trains.size();
            if (newNumberOfTrains != this.lastNumberOfTrains) {
                jList1.setModel(new WorldToListModelAdapter(world, KEY.TRAINS,
                        principal));
                if (newNumberOfTrains > 0) {
                    jList1.setSelectedIndex(0);
                }
                lastNumberOfTrains = newNumberOfTrains;
            }
        }
        super.paint(g);
    }

}
