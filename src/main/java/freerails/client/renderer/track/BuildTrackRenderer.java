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

package freerails.client.renderer.track;

import freerails.client.ClientConfig;
import freerails.client.ModelRootProperty;
import freerails.client.renderer.RendererRoot;
import freerails.util.ui.Painter;
import freerails.client.ModelRoot;
import freerails.util.Vec2D;
import freerails.model.world.ReadOnlyWorld;
import freerails.model.terrain.FullTerrainTile;
import freerails.model.track.TrackPiece;

import java.awt.*;
import java.util.Map;

/**
 * Draws the track being build.
 */
public class BuildTrackRenderer implements Painter {

    private final ModelRoot modelRoot;
    private final RendererRoot rendererRoot;

    /**
     * @param trackPieceViewList
     * @param modelRoot
     */
    public BuildTrackRenderer(RendererRoot trackPieceViewList, ModelRoot modelRoot) {
        this.modelRoot = modelRoot;
        rendererRoot = trackPieceViewList;
    }

    /**
     * Paints the proposed track and dots to distinguish the proposed track from
     * any existing track.
     */
    public void paint(Graphics2D g, Rectangle newVisibleRectangle) {

        Map<Vec2D, TrackPiece> proposedTrack = null;
        if (modelRoot != null) {
            proposedTrack = (Map<Vec2D, TrackPiece>) modelRoot.getProperty(ModelRootProperty.PROPOSED_TRACK);
        }
        if (null != proposedTrack) {
            for (Vec2D point : proposedTrack.keySet()) {
                TrackPiece trackPiece = proposedTrack.get(point);

                int graphicsNumber = trackPiece.getTrackGraphicID();

                int ruleNumber = trackPiece.getTrackTypeID();
                TrackPieceRenderer trackPieceView = rendererRoot.getTrackPieceView(ruleNumber);
                trackPieceView.drawTrackPieceIcon(g, graphicsNumber, point, ClientConfig.TILE_SIZE);
            }

            ReadOnlyWorld realWorld = modelRoot.getWorld();
            /*
             * Draw small dots for each tile whose track has changed. The dots
             * are white if track has been added or upgraded and red if it has
             * been removed.
             */
            for (Vec2D p : proposedTrack.keySet()) {
                Vec2D location = Vec2D.add(Vec2D.multiply(p, ClientConfig.TILE_SIZE), Vec2D.divide(Vec2D.subtract(ClientConfig.TILE_SIZE, ClientConfig.SMALL_DOT_WIDTH), 2));
                FullTerrainTile before = (FullTerrainTile) realWorld.getTile(p);
                TrackPiece trackPiece = proposedTrack.get(p);

                boolean trackRemoved = !trackPiece.getTrackConfiguration().contains(before.getTrackPiece().getTrackConfiguration());
                Color dotColor = trackRemoved ? Color.RED : Color.WHITE;
                g.setColor(dotColor);
                g.fillOval(location.x, location.y, ClientConfig.SMALL_DOT_WIDTH, ClientConfig.SMALL_DOT_WIDTH);
            }
        }
    }

}