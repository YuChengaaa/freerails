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
 * SelectStationJPanel.java
 *
 */

package freerails.client.view;

import freerails.client.renderer.RendererRoot;
import freerails.controller.ModelRoot;
import freerails.world.KEY;
import freerails.world.NonNullElementWorldIterator;
import freerails.world.ReadOnlyWorld;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.station.Station;
import freerails.world.terrain.FullTerrainTile;
import freerails.world.terrain.TileTransition;
import freerails.world.track.NullTrackPiece;
import freerails.world.train.MutableSchedule;
import freerails.world.train.TrainOrdersModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.NoSuchElementException;

/**
 * This JPanel lets the user select a station from a map and add it to a train
 * schedule.
 */
public class SelectStationJPanel extends JPanel implements View {

    private static final long serialVersionUID = 3258411750662877488L;
    private ReadOnlyWorld world;
    private ActionListener submitButtonCallBack;
    private int selectedStationID = 0;
    private int selectedOrderNumber = 1;
    private MutableSchedule schedule;
    private Rectangle visableMapTiles = new Rectangle();
    private double scale = 1;
    private boolean needsUpdating = true;
    private FreerailsPrincipal principal;
    private CargoWaitingAndDemandedPanel cargoWaitingAndDemandedPanel1;
    private JLabel jLabel1;
    
    public SelectStationJPanel() {
        initComponents();
    }


    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        cargoWaitingAndDemandedPanel1 = new CargoWaitingAndDemandedPanel();
        jLabel1 = new JLabel();

        setLayout(new GridBagLayout());

        setPreferredSize(new Dimension(500, 350));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                formComponentResized(e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                formComponentShown(e);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                formKeyPressed(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                formMouseClicked(e);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                formMouseMoved(e);
            }
        });

        cargoWaitingAndDemandedPanel1.setBorder(new LineBorder(new Color(0, 0, 0)));
        cargoWaitingAndDemandedPanel1.setPreferredSize(new Dimension(165, 300));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(7, 7, 7, 7);
        add(cargoWaitingAndDemandedPanel1, gridBagConstraints);

        jLabel1.setText("Train #1 Stop 1");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(jLabel1, gridBagConstraints);

    }

    private void formComponentShown(ComponentEvent evt) {
        setZoom();
    }

    private void formMouseClicked(MouseEvent evt) {
        formMouseMoved(evt);
        needsUpdating = true;
        submitButtonCallBack.actionPerformed(null);
    }

    private void formKeyPressed(KeyEvent evt) {
        try {
            TileTransition v = KeyCodeToOneTileMoveVector.getInstanceMappedToKey(evt.getKeyCode());
            // now find nearest station in direction of the vector.
            NearestStationFinder stationFinder = new NearestStationFinder(world, principal);
            int station = stationFinder.findNearestStationInDirection(selectedStationID, v);

            if (selectedStationID != station && station != NearestStationFinder.NOT_FOUND) {
                selectedStationID = station;
                cargoWaitingAndDemandedPanel1.display(selectedStationID);
                validate();
                repaint();
            }
        } catch (NoSuchElementException e) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                needsUpdating = true;
                submitButtonCallBack.actionPerformed(null);
            }
            // The key pressed isn't mapped to a OneTileMoveVector so do
            // nothing.
        }
    }

    private void formComponentResized(ComponentEvent evt) {
        setZoom();
    }

    private void formMouseMoved(MouseEvent evt) {
        // Add your handling code here:
        double x = evt.getX();
        x = x / scale + visableMapTiles.x;
        double y = evt.getY();
        y = y / scale + visableMapTiles.y;

        NearestStationFinder stationFinder = new NearestStationFinder(world, principal);
        int station = stationFinder.findNearestStation((int) x, (int) y);

        if (selectedStationID != station && station != NearestStationFinder.NOT_FOUND) {
            selectedStationID = station;
            cargoWaitingAndDemandedPanel1.display(selectedStationID);
            validate();
            repaint();

        }
    }

    public void display(MutableSchedule newSchedule, int orderNumber) {
        schedule = newSchedule;
        selectedOrderNumber = orderNumber;
        TrainOrdersModel order = newSchedule.getOrder(selectedOrderNumber);
        selectedStationID = order.getStationID();

        // Set the text on the title JLabel.
        jLabel1.setText("Stop " + String.valueOf(selectedOrderNumber + 1));

        // Set the station info panel to show the current selected station.
        cargoWaitingAndDemandedPanel1.display(selectedStationID);
    }

    /**
     * Sets the zoom based on the size of the component and the positions of the
     * stations.
     */
    private void setZoom() {
        Rectangle mapRect = getBounds();
        Rectangle r = cargoWaitingAndDemandedPanel1.getBounds();
        mapRect.width -= r.width;

        int topLeftX = Integer.MAX_VALUE;
        int topLeftY = Integer.MAX_VALUE;
        int bottomRightX = Integer.MIN_VALUE;
        int bottomRightY = Integer.MIN_VALUE;

        NonNullElementWorldIterator it = new NonNullElementWorldIterator(KEY.STATIONS, world, principal);
        while (it.next()) {
            Station station = (Station) it.getElement();
            if (station.x < topLeftX) topLeftX = station.x;
            if (station.y < topLeftY) topLeftY = station.y;
            if (station.x > bottomRightX) bottomRightX = station.x;
            if (station.y > bottomRightY) bottomRightY = station.y;
        }
        // Add some padding.
        topLeftX -= 10;
        topLeftY -= 10;
        bottomRightX += 10;
        bottomRightY += 10;

        int width = bottomRightX - topLeftX;
        int height = bottomRightY - topLeftY;
        visableMapTiles = new Rectangle(topLeftX, topLeftY, width, height);
        boolean heightConstraintBinds = (visableMapTiles.getHeight() / visableMapTiles.getWidth()) > (mapRect.getHeight() / mapRect.getWidth());
        if (heightConstraintBinds) {
            scale = mapRect.getHeight() / visableMapTiles.getHeight();
        } else {
            scale = mapRect.getWidth() / visableMapTiles.getWidth();
        }
        needsUpdating = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (needsUpdating) {
            setZoom();
        }

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        NonNullElementWorldIterator it = new NonNullElementWorldIterator(KEY.STATIONS, world, principal);

        // Draw track
        g2.setColor(Color.BLACK);
        for (int x = Math.max(0, visableMapTiles.x); x < Math.min(visableMapTiles.width + visableMapTiles.x, world.getMapWidth()); x++) {
            for (int y = Math.max(0, visableMapTiles.y); y < Math.min(visableMapTiles.height + visableMapTiles.y, world.getMapHeight()); y++) {
                FullTerrainTile tt = (FullTerrainTile) world.getTile(x, y);
                if (!tt.getTrackPiece().equals(NullTrackPiece.getInstance())) {
                    double xDouble = x - visableMapTiles.x;
                    xDouble = xDouble * scale;
                    double yDouble = y - visableMapTiles.y;
                    yDouble = yDouble * scale;
                    g.drawRect((int) xDouble, (int) yDouble, 1, 1);
                }
            }
        }
        // Draw stations
        while (it.next()) {

            /*
             * (1) The selected station is drawn green. (2) Non-selected
             * stations which are on the schedule are drawn blue. (3) Other
             * stations are drawn white. (4) If, for instance, station X is the
             * first stop on the schedule, "1" is drawn above the station. (5)
             * If, for instance, station X is the first and third stop on the
             * schedule, "1, 3" is drawn above the station. (6) The stop numbers
             * drawn above the stations are drawn using the same colour as used
             * to draw the station.
             */
            Station station = (Station) it.getElement();
            double x = station.x - visableMapTiles.x;
            x = x * scale;
            double y = station.y - visableMapTiles.y;
            y = y * scale;
            int xInt = (int) x;
            int yInt = (int) y;

            StringBuilder stopNumbersString = new StringBuilder();
            boolean stationIsOnSchedule = false;
            for (int orderNumber = 0; orderNumber < schedule.getNumOrders(); orderNumber++) {
                int stationID = orderNumber == selectedOrderNumber ? selectedStationID : schedule.getOrder(orderNumber).getStationID();
                if (it.getIndex() == stationID) {
                    if (stationIsOnSchedule) {
                        stopNumbersString.append(", ").append(String.valueOf(orderNumber + 1));
                    } else {
                        stopNumbersString = new StringBuilder(String.valueOf(orderNumber + 1));
                    }
                    stationIsOnSchedule = true;
                }
            }
            if (stationIsOnSchedule) {
                if (it.getIndex() == selectedStationID) {
                    g2.setColor(Color.GREEN);
                } else {
                    g2.setColor(Color.BLUE);
                }
                g2.drawString(stopNumbersString.toString(), xInt, yInt - 4);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.fillRect(xInt, yInt, 10, 10);
        }
    }

    public void setup(ModelRoot modelRoot, RendererRoot vl, Action closeAction) {
        cargoWaitingAndDemandedPanel1.setup(modelRoot, vl, null);
        world = modelRoot.getWorld();
        submitButtonCallBack = closeAction;
        principal = modelRoot.getPrincipal();
    }

    public MutableSchedule generateNewSchedule() {
        TrainOrdersModel oldOrders, newOrders;
        oldOrders = schedule.getOrder(selectedOrderNumber);
        newOrders = new TrainOrdersModel(selectedStationID, oldOrders.getConsist(), oldOrders.getWaitUntilFull(), oldOrders.isAutoConsist());
        schedule.setOrder(selectedOrderNumber, newOrders);
        return schedule;
    }

}
