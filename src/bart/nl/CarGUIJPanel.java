package bart.nl;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * This JPanel is used on the NetBeans controlled UI class CarGUI.
 * It replaces the JPanel boxPanel with a custom creation code.
 * 
 * @author Bart Jansen
 */
public class CarGUIJPanel extends JPanel {

    private int controlX, controlY;
    private final int controlSize = 16;
    private int circleSize;
    private int radiusBigCircle;
    private int centerX, centerY;
    private boolean movingToCenter = false;
    private Color color = Color.RED;
    private final Color colorBigCircle = Color.BLACK;

    @Override
    protected void paintComponent(Graphics g) {
        if (circleSize == 0) {
            circleSize = getWidth() - 10;
            radiusBigCircle = circleSize / 2;
        }
        g.setColor(colorBigCircle);
        super.paintComponent(g);
        g.drawOval(5, 5, circleSize, circleSize);
        g.setColor(color);
        g.fillOval(controlX, controlY, controlSize, controlSize);

    }

    /**
     * Move the small circle with X and Y. The circle is restricted to the larger,
     * surrounding circle.
     *
     * @param deltaX The amount X needs to change.
     * @param deltaY The amount Y needs to change.
     */
    public void moveXY(int deltaX, int deltaY) {
        movingToCenter = false;
        color = Color.GREEN;
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
        movingToCenter = true;
        Thread t = new Thread(new MoveCircleSlowlyToCentre());
        t.start();

    }

    public boolean circleInCentre() {
        if (controlX == centerX && controlY == centerY) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Checks whether the little circle is in the center.
     * If it's not and the user did not initiate an action to move it, the class
     * will slowly move the circle back to the center.
     * After the movement, the circle is repainted with a red color.
     */
    private class MoveCircleSlowlyToCentre implements Runnable {

        private final int step = 3;
        private final int delay = 20;

        @Override
        public void run() {
            while ((controlX != centerX || controlY != centerY) && movingToCenter) {
                if (controlX < centerX) {
                    controlX = controlX + step;
                    if (controlX > centerX) {
                        controlX = centerX;
                    }
                } else if (controlX > centerX) {
                    controlX = controlX - step;
                    if (controlX < centerX) {
                        controlX = centerX;
                    }
                }

                if (controlY < centerY) {
                    controlY = controlY + step;
                    if (controlY > centerY) {
                        controlY = centerY;
                    }
                } else if (controlY > centerY) {
                    controlY = controlY - step;
                    if (controlY < centerY) {
                        controlY = centerY;
                    }
                }
                color = Color.ORANGE;
                repaint();

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {

                }

            }
            color = Color.RED;
            repaint();
        }

    }

}
