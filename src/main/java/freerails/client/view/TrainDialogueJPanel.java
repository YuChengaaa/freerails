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
 * TrainDialogueJPanel.java
 *
 */

package freerails.client.view;

import freerails.client.renderer.RendererRoot;
import freerails.controller.ModelRoot;
import freerails.world.*;
import freerails.world.player.FreerailsPrincipal;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * JPanel that displays info on a train; it is composed of a
 * {@link TrainScheduleJPanel} and {@link TrainDescriptionJPanel}.
 */

public class TrainDialogueJPanel extends javax.swing.JPanel implements View, WorldListListener {

    private static final long serialVersionUID = 3257005466801157938L;

    private static final Logger logger = Logger.getLogger(TrainDialogueJPanel.class.getName());
    javax.swing.JButton closeJButton;
    freerails.client.view.TrainScheduleJPanel newTrainScheduleJPanel1;
    javax.swing.JButton nextJButton;
    javax.swing.JButton previousJButton;
    freerails.client.view.TrainDescriptionJPanel trainDetailsJPanel1;
    javax.swing.JButton trainListJButton;
    private WorldIterator wi;
    private ReadOnlyWorld w;
    private FreerailsPrincipal principal;

    public TrainDialogueJPanel() {
        initComponents();
    }


    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        newTrainScheduleJPanel1 = new freerails.client.view.TrainScheduleJPanel();
        trainDetailsJPanel1 = new TrainDescriptionJPanel();
        previousJButton = new javax.swing.JButton();
        nextJButton = new javax.swing.JButton();
        trainListJButton = new javax.swing.JButton();
        closeJButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(510, 400));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(newTrainScheduleJPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(trainDetailsJPanel1, gridBagConstraints);

        previousJButton.setText("last");
        previousJButton.addActionListener(this::previousJButtonActionPerformed);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(previousJButton, gridBagConstraints);

        nextJButton.setText("next");
        nextJButton.addActionListener(this::nextJButtonActionPerformed);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(nextJButton, gridBagConstraints);

        trainListJButton.setText("Train list");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(trainListJButton, gridBagConstraints);

        closeJButton.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(closeJButton, gridBagConstraints);

    }

    private void previousJButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        if (wi.previous()) {
            display(wi.getIndex());
        } else {
            logger.warn("Couldn't get previous");
        }
    }

    private void nextJButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        if (wi.next()) {
            display(wi.getIndex());
        } else {
            logger.warn("Couldn't get next");
        }
    }

    public void setup(ModelRoot modelRoot, RendererRoot vl, Action closeAction) {
        newTrainScheduleJPanel1.setup(modelRoot, vl, closeAction);
        trainDetailsJPanel1.setup(modelRoot, vl, closeAction);
        setCancelButtonActionListener(closeAction);
        principal = modelRoot.getPrincipal();
        w = modelRoot.getWorld();
    }

    public void display(int trainNumber) {
        wi = new NonNullElementWorldIterator(KEY.TRAINS, w, principal);
        wi.gotoIndex(trainNumber);
        if (wi.getRowID() > 0) {
            previousJButton.setEnabled(true);
        } else {
            previousJButton.setEnabled(false);
        }

        if (wi.getRowID() < (wi.size() - 1)) {
            nextJButton.setEnabled(true);
        } else {
            nextJButton.setEnabled(false);
        }

        newTrainScheduleJPanel1.display(trainNumber);
        trainDetailsJPanel1.displayTrain(trainNumber);
    }

    public void listUpdated(KEY key, int index, FreerailsPrincipal principal) {
        newTrainScheduleJPanel1.listUpdated(key, index, principal);
    }

    public void itemAdded(KEY key, int index, FreerailsPrincipal principal) {
    }

    public void itemRemoved(KEY key, int index, FreerailsPrincipal principal) {
    }

    void setTrainDetailsButtonActionListener(ActionListener l) {
        ActionListener[] oldListeners = trainListJButton.getActionListeners();
        for (ActionListener oldListener : oldListeners) {
            trainListJButton.removeActionListener(oldListener);
        }
        trainListJButton.addActionListener(l);
    }

    /**
     * Removes any existing ActionListener listeners from the cancel button,
     * then adds the specified one.
     */
    void setCancelButtonActionListener(ActionListener l) {
        ActionListener[] oldListeners = closeJButton.getActionListeners();
        for (ActionListener oldListener : oldListeners) {
            closeJButton.removeActionListener(oldListener);
        }
        closeJButton.addActionListener(l);
    }

}
