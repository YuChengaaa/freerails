/*
 * TrainReport.java
 *
 * Created on 30 March 2003, 02:20
 */

package experimental;
import java.awt.event.KeyEvent;
/**
 *
 * @author  Luke
 */
public class TrainReport extends javax.swing.JFrame {
    
    /** Creates new form TrainReport */
    public TrainReport() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        trainOrders1 = new jfreerails.client.view.TrainOrders();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        trainOrders2 = new jfreerails.client.view.TrainOrders();
        trainOrders3 = new jfreerails.client.view.TrainOrders();
        trainOrders4 = new jfreerails.client.view.TrainOrders();
        trainOrders5 = new jfreerails.client.view.TrainOrders();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TRAIN ORDERS");
        jLabel1.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Priority Orders");
        jLabel12.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel12.add(jLabel12);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("Priority Consist:");
        jPanel12.add(jLabel22);

        getContentPane().add(jPanel12);

        trainOrders1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trainOrders1KeyPressed(evt);
            }
        });

        getContentPane().add(trainOrders1);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Scheduled Stops");
        jLabel11.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel11.add(jLabel11);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("New Consist");
        jPanel11.add(jLabel21);

        getContentPane().add(jPanel11);

        trainOrders2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trainOrders2KeyPressed(evt);
            }
        });

        getContentPane().add(trainOrders2);

        trainOrders3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trainOrders3KeyPressed(evt);
            }
        });

        getContentPane().add(trainOrders3);

        trainOrders4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trainOrders4KeyPressed(evt);
            }
        });

        getContentPane().add(trainOrders4);

        trainOrders5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trainOrders5KeyPressed(evt);
            }
        });

        getContentPane().add(trainOrders5);

        pack();
    }//GEN-END:initComponents
    
    private void trainOrders5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trainOrders5KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            this.trainOrders4.requestFocus();
        }
    }//GEN-LAST:event_trainOrders5KeyPressed
    
    private void trainOrders4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trainOrders4KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            this.trainOrders3.requestFocus();
        }
        if(evt.getKeyCode()==KeyEvent.VK_DOWN){
            this.trainOrders5.requestFocus();
        }
    }//GEN-LAST:event_trainOrders4KeyPressed
    
    private void trainOrders3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trainOrders3KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            this.trainOrders2.requestFocus();
        }
        if(evt.getKeyCode()==KeyEvent.VK_DOWN){
            this.trainOrders4.requestFocus();
        }
    }//GEN-LAST:event_trainOrders3KeyPressed
    
    private void trainOrders2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trainOrders2KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            this.trainOrders1.requestFocus();
        }
        if(evt.getKeyCode()==KeyEvent.VK_DOWN){
            this.trainOrders3.requestFocus();
        }
    }//GEN-LAST:event_trainOrders2KeyPressed
    
    private void trainOrders1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trainOrders1KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_DOWN){
            this.trainOrders2.requestFocus();
        }
    }//GEN-LAST:event_trainOrders1KeyPressed
        
    
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new TrainReport().show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel12;
    private jfreerails.client.view.TrainOrders trainOrders1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private jfreerails.client.view.TrainOrders trainOrders5;
    private javax.swing.JLabel jLabel11;
    private jfreerails.client.view.TrainOrders trainOrders2;
    private javax.swing.JPanel jPanel1;
    private jfreerails.client.view.TrainOrders trainOrders3;
    private javax.swing.JLabel jLabel22;
    private jfreerails.client.view.TrainOrders trainOrders4;
    // End of variables declaration//GEN-END:variables
    
}