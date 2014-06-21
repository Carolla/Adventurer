package hic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import civ.MainframeCiv;

/**
 * The BuildingRectangle class groups the normal Rectangle functionality
 * with the ability to draw the Rectangle on its parent frame.
 * 
 * @author Tim Armstrong
 *
 */
@SuppressWarnings("serial")
public class BuildingRectangle extends Rectangle
{
    private static final int RECT_X = 0;
    private static final int RECT_Y = 1;
    private static final int RECT_WIDTH = 2;
    private static final int RECT_HEIGHT = 3;
    private static final int STROKE_WIDTH = 4;

    private final Color _color;
    private final String _name;

    /**
     * A rectangle that hold properties for display
     * @param name the name that appears in the rectangle
     * @param c the color of the rectangle
     * @param buildingLayouts the dimensions of the rectangle
     * @param advMainframeCiv the parent frame
     */
    public BuildingRectangle(String name, Color c, Dimension panelSize, float[] buildingLayouts) {
        _name = name;
        _color = c;
        x = (int) (panelSize.width * buildingLayouts[RECT_X]);
        y = (int) (panelSize.height * buildingLayouts[RECT_Y]);
        width = (int) (panelSize.width * buildingLayouts[RECT_WIDTH]);
        height = (int) (panelSize.height * buildingLayouts[RECT_HEIGHT]);
    }

    /**
     * Draw the rectangle.
     * @param g the graphics object to draw the rectangle with
     */
    public void drawBuildingBox(Graphics2D g)
    {
        Font font = new Font("Serif", Font.BOLD, 20);
        FontMetrics fm = g.getFontMetrics(font);
        g.setColor(_color);
        g.setStroke(new BasicStroke(STROKE_WIDTH));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);

        int Zwidth = fm.stringWidth(_name);
        int cx = (int) (getCenterX() - (Zwidth / 2));
        int cy = (int) (getMinY() - fm.getHeight());
//        int cy = (int) (getCenterY() + (fm.getHeight() / 2) - fm.getAscent());
        g.drawString(_name, cx, cy);
        g.draw(this);
    }
}
