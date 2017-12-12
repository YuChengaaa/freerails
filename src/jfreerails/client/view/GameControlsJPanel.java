/*
 * GameControlsJPanel.java
 *
 * Created on 19 April 2003, 16:41
 */

package jfreerails.client.view;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jfreerails.client.renderer.ViewLists;
import jfreerails.world.top.ReadOnlyWorld;

/**
 *  This JPanel shows the games controls, that is, which keys do what.  Currently, it just displays
 *a hard coded html string.
 * @author  Luke
 */
public class GameControlsJPanel extends javax.swing.JPanel implements View {

	private ReadOnlyWorld w;

	/** Creates new form GameControlsJPanel */
	public GameControlsJPanel() {
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() { //GEN-BEGIN:initComponents
		jLabel1 = new javax.swing.JLabel();
		loadText();
		done = new javax.swing.JButton();

		setLayout(
			new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

		add(jLabel1);

		done.setText("Close");
		add(done);

	} //GEN-END:initComponents

	public void setup(
		ReadOnlyWorld w,
		ViewLists vl,
		ActionListener submitButtonCallBack) {
		this.w = w;
		this.done.addActionListener(submitButtonCallBack);
	}

	/** Load the help text from file.  */
	public void loadText() {
		try {
			java.net.URL url =
				GameControlsJPanel.class.getResource(
					"/jfreerails/client/view/game_controls.html");
			InputStream in = url.openStream();
			BufferedReader br =
				new BufferedReader(
					new InputStreamReader(new DataInputStream(in)));
			String line;
			String text = "";
			while ((line = br.readLine()) != null) {
				text = text + line;
			}
			this.jLabel1.setText(text);
		} catch (IOException e) {
			e.printStackTrace();
			this.jLabel1.setText("Couldn't load file.");
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton done;
	private javax.swing.JLabel jLabel1;
	// End of variables declaration//GEN-END:variables

}
