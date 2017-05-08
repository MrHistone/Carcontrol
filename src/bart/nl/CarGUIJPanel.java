package bart.nl;

import java.awt.Graphics;
import javax.swing.JPanel;

public class CarGUIJPanel extends JPanel {

    private int controlX, controlY;
    private int controlSize = 16;
    private int circleSize;
    private int radiusBigCircle;
    private int centerX, centerY;

    @Override
    protected void paintComponent(Graphics g) {
        if (circleSize == 0) {
            circleSize = getWidth() - 10;
            radiusBigCircle = circleSize / 2;
        }

        super.paintComponent(g);
        g.drawOval(5, 5, circleSize, circleSize);
        g.fillOval(controlX, controlY, controlSize, controlSize);

    }

    protected void setXY(int x, int y) {
        this.controlX = x;
        this.controlY = y;
    }

    protected int getControlX() {
        return controlX;
    }

    protected int getControlY() {
        return controlY;
    }

    /**
     * Move the small circle with X and Y.
     * The circle is bound to the larger, surrounding circle.
     *
     * @param deltaX The amount X needs to change.
     * @param deltaY The amount Y needs to change.
     */
    public void moveXY(int deltaX, int deltaY) {
        // Get X and Y relative to center of big circle. (Center = 0)
        // Take into account the radius of the small circle and tweak a little bit.
        int relativeX = ((Math.max(controlX + deltaX, centerX) - Math.min(controlX + deltaX, centerX)) + (controlSize / 2)) - 2;
        int relativeY = ((Math.max(controlY + deltaY, centerY) - Math.min(controlY + deltaY, centerY)) + (controlSize / 2)) - 2;
        // Calculate the hypotenuse.
        double hypotenuse = Math.hypot(relativeX, relativeY);
        // If hypotenuse gets larger (with new X and/or Y) than the radius of the big circle, cancel the action.
        if (hypotenuse > radiusBigCircle) {
            // X and/or Y cannot get bigger without getting outside the boundary.
        } else {
            controlX = controlX + deltaX;
            controlY = controlY + deltaY;
            repaint();
        }

    }

    protected void centerControl() {
        if (centerX == 0) {
            controlX = getWidth() / 2;
            controlX = controlX - controlSize / 2;
            controlY = getHeight() / 2;
            controlY = controlY - controlSize / 2;
            centerX = controlX;
            centerY = controlY;
        }
        controlX = centerX;
        controlY = centerY;
        repaint();
    }

    public boolean circleInCentre(){
        if (controlX == centerX && controlY == centerY){
            return true;
        } else {
            return false;
        }
        
    }
    
    
    
}
