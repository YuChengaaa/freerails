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
 */

package freerails.client.view;

import freerails.client.renderer.RendererRoot;
import freerails.controller.ModelRoot;
import freerails.world.KEY;
import freerails.world.NonNullElementWorldIterator;
import freerails.world.ReadOnlyWorld;
import freerails.world.WorldIterator;
import freerails.world.player.FreerailsPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * JPanel that displays a list of trains, used for the train list window and the
 * train roaster tab.
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
    private ActionListener showTrainDetails = e -> {

    };
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


    private void initComponents() {
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
        showDetails.addActionListener(this::showDetailsActionPerformed);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(showDetails, gridBagConstraints);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setCellRenderer(trainSummaryJPanel1);
        jList1.setDoubleBuffered(true);
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                jList1KeyPressed(e);
            }
        });
        jList1.addListSelectionListener(this::jList1ValueChanged);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                jList1MouseClicked(e);
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

    }


    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
        // if a train is selected, enable the 'show details' button.
        if (jList1.getSelectedIndex() != -1) {
            showDetails.setEnabled(true);
        } else {
            showDetails.setEnabled(false);
        }
    }

    private void showDetailsActionPerformed(java.awt.event.ActionEvent evt) {
        showTrainDetails.actionPerformed(evt);
    }

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {
        // Add your handling code here:
        if (evt.getClickCount() == 2) {
            showTrainDetails.actionPerformed(null);
        }
    }

    private void jList1KeyPressed(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            showTrainDetails.actionPerformed(null);
        }
    }

    public void setup(ModelRoot modelRoot, RendererRoot vl, Action closeAction) {
        world = modelRoot.getWorld();
        trainSummaryJPanel1.setup(modelRoot, vl, null);

        if (rhsjTabPane) {
            jList1.setModel(new WorldToListModelAdapter(modelRoot.getWorld(), KEY.TRAINS, modelRoot.getPrincipal()));
            TrainListCellRenderer trainView = new TrainListCellRenderer(modelRoot, vl);
            jList1.setCellRenderer(trainView);
            trainView.setHeight(trainViewHeight);
        }

        ActionListener[] oldListeners = closeJButton.getActionListeners();
        for (ActionListener oldListener : oldListeners) {
            closeJButton.removeActionListener(oldListener);
        }
        closeJButton.addActionListener(closeAction);
        principal = modelRoot.getPrincipal();
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
        return NonNullElementWorldIterator.row2index(world, KEY.TRAINS, principal, row);
    }


    /**
     * When the train list is shown on a tab we don't want the buttons.
     */
    void removeButtons() {
        removeAll();

        java.awt.GridBagConstraints gridBagConstraints;
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

    }

    /**
     * @param trainViewHeight
     */
    public void setTrainViewHeight(int trainViewHeight) {
        this.trainViewHeight = trainViewHeight;
    }

    @Override
    public void paint(Graphics g) {
        if (null != world) {
            WorldIterator trains = new NonNullElementWorldIterator(KEY.TRAINS, world, principal);
            int newNumberOfTrains = trains.size();
            if (newNumberOfTrains != lastNumberOfTrains) {
                jList1.setModel(new WorldToListModelAdapter(world, KEY.TRAINS, principal));
                if (newNumberOfTrains > 0) {
                    jList1.setSelectedIndex(0);
                }
                lastNumberOfTrains = newNumberOfTrains;
            }
        }
        super.paint(g);
    }

}
