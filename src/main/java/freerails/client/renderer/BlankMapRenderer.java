package freerails.client.renderer;

import java.awt.*;

/**
 * Used for testing the Map view components without setting up any map data.
 *
 */
public class BlankMapRenderer implements MapRenderer {
    private final float scale;

    /**
     *
     * @param s
     */
    public BlankMapRenderer(float s) {
        scale = s;
    }

    /**
     *
     * @return
     */
    public float getScale() {
        return scale;
    }

    /**
     *
     * @return
     */
    public Dimension getMapSizeInPixels() {
        int height = (int) (400 * scale);
        int width = (int) (400 * scale);

        return new Dimension(height, width);
    }

    /**
     *
     * @param g
     * @param tileX
     * @param tileY
     */
    public void paintTile(Graphics g, int tileX, int tileY) {
        paintRect(g, null);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void refreshTile(int x, int y) {
    }

    /**
     *
     * @param g
     * @param visibleRect
     */
    public void paintRect(Graphics g, Rectangle visibleRect) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, (int) (scale * 400), (int) (scale * 400));
        g.setColor(Color.blue);

        int x = (int) (100 * scale);
        int y = (int) (100 * scale);
        int height = (int) (200 * scale);
        int width = (int) (200 * scale);
        g.fillRect(x, y, height, width);
    }

    /**
     *
     */
    public void refreshAll() {
        // do nothing
    }
}