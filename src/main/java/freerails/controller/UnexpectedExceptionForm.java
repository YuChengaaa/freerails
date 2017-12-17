/*
 * UnexpectedExceptionForm.java
 *
 * Created on 09 September 2005, 21:44
 */

package freerails.controller;

/**
 * @author Luke
 */
public class UnexpectedExceptionForm extends javax.swing.JFrame {

    private static final long serialVersionUID = -4348641764811196495L;

    /**
     * Creates new form UnexpectedExceptionForm
     */
    public UnexpectedExceptionForm() {
        initComponents();
    }

    public void setText(String s) {
        copyableTextJPanel1.setText(s);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        copyableTextJPanel1 = new freerails.controller.CopyableTextJPanel();
        closebutton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Unexpected Exception");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(copyableTextJPanel1, gridBagConstraints);

        closebutton.setText("Close");
        closebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebuttonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(closebutton, gridBagConstraints);

        pack();
    }

    // </editor-fold>//GEN-END:initComponents

    private void closebuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_closebuttonActionPerformed
        System.exit(1);
    }// GEN-LAST:event_closebuttonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UnexpectedExceptionForm unexpectedExceptionForm = new UnexpectedExceptionForm();
                Exception e = new Exception("Oh No..");
                String str = ReportBugTextGenerator.genText(e);
                unexpectedExceptionForm.setText(str);
                unexpectedExceptionForm.setVisible(true);
                e.printStackTrace();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton closebutton;
    freerails.controller.CopyableTextJPanel copyableTextJPanel1;
    // End of variables declaration//GEN-END:variables

}