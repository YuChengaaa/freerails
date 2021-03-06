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
 *
 */
package freerails.client.renderer.tile;

import freerails.util.Vec2D;
import freerails.util.ui.ImageManager;
import freerails.model.world.ReadOnlyWorld;
import freerails.model.terrain.TerrainType;

import java.io.File;
import java.io.IOException;

/**
 * Paints a tile for which there only one tile icon.
 */
public class StandardTileRenderer extends AbstractTileRenderer {

    /**
     * @param imageManager
     * @param rgbValues
     * @param terrainType
     * @throws IOException
     */
    public StandardTileRenderer(ImageManager imageManager, int[] rgbValues, TerrainType terrainType) throws IOException {
        super(terrainType, rgbValues, 1);
        getTileIcons()[0] = imageManager.getImage(generateFilename());
    }

    private String generateFilename() {
        return "terrain" + File.separator + getTerrainTypeName() + ".png";
    }

    @Override
    public int selectTileIconIndex(Vec2D mapLocation, ReadOnlyWorld world) {
        return 0;
    }

    /**
     * @param i
     * @return
     */
    @Override
    protected String generateFileNameNumber(int i) {
        throw new UnsupportedOperationException();
    }
}