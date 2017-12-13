/*
 * TrainListJPanel.java
 *
 * Created on 18 February 2004, 23:13
 */

package jfreerails.client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import jfreerails.client.renderer.ViewLists;
import jfreerails.world.top.KEY;
import jfreerails.world.top.ReadOnlyWorld;
/**
 *
 * @author  Luke
 */
public class TrainListJPanel extends javax.swing.JPanel implements View {
	
	private ReadOnlyWorld world;
    
    /** Creates new form TrainListJPanel */
    public TrainListJPanel() {
        initComponents();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jList1 = new javax.swing.JList();
        closeJButton = new javax.swing.JButton();
        showDetails = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList1KeyPressed(evt);
            }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jList1, gridBagConstraints);

        closeJButton.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.weightx = 1.0;
        add(closeJButton, gridBagConstraints);

        showDetails.setText("Show details");
        showDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDetailsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.weightx = 1.0;
        add(showDetails, gridBagConstraints);

    }//GEN-END:initComponents

    private void showDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDetailsActionPerformed
       showTrainDetails.actionPerformed(evt);
    }//GEN-LAST:event_showDetailsActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // Add your handling code here:
        if(evt.getClickCount()==2){
            showTrainDetails.actionPerformed(null);
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jList1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            showTrainDetails.actionPerformed(null);
        }
    }//GEN-LAST:event_jList1KeyPressed
    
    public void setup(ReadOnlyWorld w, ViewLists vl, ActionListener submitButtonCallBack) {  
		world = w;
        jList1.setModel(new World2ListModelAdapter(w, KEY.TRAINS));
        TrainViewJPanel trainView =
        new TrainViewJPanel(w, vl);
        jList1.setCellRenderer(trainView);
        trainView.setHeight(50);               
        ActionListener[] oldListeners = closeJButton.getActionListeners();
        for(int i = 0; i < oldListeners.length; i ++){
            closeJButton.removeActionListener(oldListeners[i]);
        }
        closeJButton.addActionListener(submitButtonCallBack);
    }
    
    void setShowTrainDetailsActionListener(ActionListener l){        
       showTrainDetails = l;
       
    }
    
    
    
    private ActionListener showTrainDetails = new ActionListener(){
        public void actionPerformed(ActionEvent arg0) {
            System.out.println(jList1.getSelectedIndex());
	}
    };
    
    int getSelectedTrainID(){
        return jList1.getSelectedIndex();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeJButton;
    private javax.swing.JList jList1;
    private javax.swing.JButton showDetails;
    // End of variables declaration//GEN-END:variables
    
	
	public void setVisible(boolean aFlag) {
		if(aFlag){
			jList1.setModel(new World2ListModelAdapter(world, KEY.TRAINS));
		}
		super.setVisible(aFlag);
	}

}
