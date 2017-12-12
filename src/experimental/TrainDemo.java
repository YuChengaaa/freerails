/*
 * TrackView.java
 *
 * Created on 23 September 2002, 20:09
 */

package experimental;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import jfreerails.client.train.TrainTypeView;
import jfreerails.client.train.TrainView;
import jfreerails.client.train.ViewPerspective;
import jfreerails.lib.GameModel;
import jfreerails.world.flat.OneTileMoveVector;
import jfreerails.world.train.FreerailsPathIterator;
import jfreerails.world.train.FreerailsPathIteratorImpl;
import jfreerails.world.train.IntLine;
import jfreerails.world.train.PathWalker;
import jfreerails.world.train.PathWalkerImpl;
/**
 *
 * @author  Luke Lindsay
 */
public class TrainDemo extends javax.swing.JPanel implements GameModel {

	TrainView trainView = new TrainView();

	int distanceTravelled = 0;

	long lastTime = System.currentTimeMillis();

	ArrayList points = new ArrayList();

	TrainTypeView[] train =
		{
			TrainTypeView.SLOW_FREIGHT,
			TrainTypeView.BULK_FREIGHT,
			TrainTypeView.FAST_FREIGHT,
			TrainTypeView.PASSENGER,
			TrainTypeView.PASSENGER,
			TrainTypeView.MAIL,
			TrainTypeView.ENGINE };

	/** Creates new form TrackView */
	public TrainDemo() {
		initComponents();
	}

	public void update() {
		long now = System.currentTimeMillis();
		long deltaTime = now - lastTime;
		distanceTravelled += 0.1 * deltaTime; //Move at 100 pixels per second.
		lastTime = now;
	}

	protected void paintComponent(Graphics g) {

		trainView.setViewPerspective(ViewPerspective.OVERHEAD);

		super.paintComponent(g);
		g.drawString("Lay track by clicking the mouse on the area below!", 10, 15);
		FreerailsPathIterator it = pathIterator();
		IntLine line = new IntLine();
		while (it.hasNext()) {
			it.nextSegment(line);
			g.drawLine(line.x1, line.y1, line.x2, line.y2);
		}

		PathWalker pw = new PathWalkerImpl(pathIterator());

		pw.stepForward(distanceTravelled);
		double actualDistance = 0;
		while (pw.hasNext()) {
			pw.nextSegment(line);
			actualDistance += line.getLength();
		}
		if (actualDistance + 10 < distanceTravelled) {
			distanceTravelled = 0;
		}
		Graphics2D g2 = (Graphics2D) g;

		IntLine wagon = new IntLine();
		for (int i = 0; i < train.length; i++) {
			/*
			g2.setStroke(new BasicStroke(3));
			if (0 == (i % 2)) {
				g2.setColor(Color.red);
			} else {
				g2.setColor(Color.green);
			}
			*/
			trainView.setTrainTypes(train[i]);
			pw.stepForward(16);
			boolean firstIteration = true;
			while (pw.hasNext()) {

				pw.nextSegment(line);
				if (firstIteration) {
					wagon.x1 = line.x1;
					wagon.y1 = line.y1;
					firstIteration = false;
				}

				//g2.drawLine(line.x1, line.y1, line.x2, line.y2);
			}
			wagon.x2 = line.x2;
			wagon.y2 = line.y2;
			OneTileMoveVector v =
				OneTileMoveVector.getNearestVector(wagon.x2 - wagon.x1, wagon.y2 - wagon.y1);
			trainView.setDirection(v);
			Point p = new Point((wagon.x2 + wagon.x1) / 2, (wagon.y2 + wagon.y1) / 2);
			trainView.drawTrain(g, p);

			//The gap between wagons
			pw.stepForward(8);
			while (pw.hasNext()) {
				pw.nextSegment(line);
			}
		}

	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() { //GEN-BEGIN:initComponents

		setLayout(null);

		addMouseListener(new java.awt.event.MouseAdapter() {

			public void mousePressed(java.awt.event.MouseEvent evt) {
				formMouseClicked(evt);
			}
		});

	} //GEN-END:initComponents

	private void formMouseClicked(
		java.awt.event.MouseEvent evt) { //GEN-FIRST:event_formMouseClicked
		// Add your handling code here:
		Point p = new Point(evt.getX(), evt.getY());
		points.add(p);
		this.repaint();

	} //GEN-LAST:event_formMouseClicked

	public FreerailsPathIterator pathIterator() {
		return new FreerailsPathIteratorImpl(points);
	}

}
