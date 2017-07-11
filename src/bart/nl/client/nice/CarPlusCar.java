package bart.nl.client.nice;

import carcontrol.Defaults.CarStatus;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class CarPlusCar extends javax.swing.JPanel {

    private BufferedImage car;
    private int topLeftCarX = 0;
    private int topLeftCarY = 0;
    private AffineTransform transform;
    private boolean firstTime = true;
    private Point position = new Point();
    private float orientation;
    private double angle;
    private final double maxAngle = 45;
    private final int maxVerticalMovement = 75;
    private CarStatus carStatus;
    private boolean continueMotionToCentre = true;

    /**
     * Creates new form CarPlusCar
     */
    public CarPlusCar() {
        initComponents();
        if (!java.beans.Beans.isDesignTime()) {
            car = LoadImage("images//car.png");
            angle = 0;
            carStatus = new CarStatus();

            Thread scanThread = new Thread(new MoveCar());
            scanThread.setName("CarScanThread");
            scanThread.start();
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (java.beans.Beans.isDesignTime()) {
        } else if (firstTime) {
            firstTime = false;
            if (topLeftCarX == 0) {
                topLeftCarX = (this.getWidth() / 2) - (car.getWidth() / 2);
                position.x = topLeftCarX;
            }
            if (topLeftCarY == 0) {
                topLeftCarY = (this.getHeight() / 2) - (car.getHeight() / 2);
                position.y = topLeftCarY;
            }

            transform = AffineTransform.getTranslateInstance(topLeftCarX, topLeftCarY);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(car, transform, null);

    }

    private BufferedImage LoadImage(String FileName) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    public void moveXY(int x, int y) {
        System.out.println("moveXY");
        boolean allowRotation = false;
        if (x != 0) {
            System.out.println("moveXY X: " + x);
            angle += x;
            if (x < 0 && angle > maxAngle * -1) {
                // Rotate left
                allowRotation = true;
            }

            if (x > 0 && angle < maxAngle) {
                // Rotate right
                allowRotation = true;
            }
            if (allowRotation) {
                rotate(angle);
            }

            if (x < 0) {

            } else if (x > 0) {

            }

        }

        if (y != 0) {
            translate(0, y);
        }

//        if (y != 0) {
//            // Unrotate and translate, then rotate again.
//            double rot = Math.atan2(transform.getShearY(), transform.getScaleY());
//            transform.rotate(0, car.getWidth() / 2, car.getHeight() / 2);
//            transform.translate(0, y);
//            transform.rotate(rot, car.getWidth() / 2, car.getHeight() / 2);
////            at.setToTranslation(0, y);
//            repaint();
//        }
    }

    public void moveCarStatus(CarStatus carStatus) {
        // The movement has been changed.
        this.carStatus = carStatus;
//        System.out.println("Forward: " + this.carStatus.isForward()
//                + "\tBackward: " + this.carStatus.isBackward()
//                + "\tLeft: " + this.carStatus.isLeft()
//                + "\tRight: " + this.carStatus.isRight());

    }

    private class MoveCar implements Runnable {

        public boolean keepGoing = true;

        @Override
        public void run() {
            // Continuous scan of the CarStatus...
            while (keepGoing) {
                if (carStatus.isBackward() == true) {
                    if ((topLeftCarY + maxVerticalMovement) > position.y) {
                        translate(0, 1);
                    }
                    checkMotionBoolean();
                }

                if (carStatus.isForward() == true) {
                    if ((topLeftCarY - maxVerticalMovement) < position.y) {
                        translate(0, -1);
                    }
                    checkMotionBoolean();
                }

                if (carStatus.isRight() == true) {
                    angle += 1;
                    if (angle <= maxAngle) {
                        rotate(angle);
                    } else {
                        angle -= 1;
                    }
                    checkMotionBoolean();
                }
                if (carStatus.isLeft() == true) {
                    angle -= 1;
                    if (angle >= -maxAngle) {
                        rotate(angle);
                    } else {
                        angle += 1;
                    }
                    checkMotionBoolean();
                }

                // Sleep is needed, otherwise it doesn't work.
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarPlusCar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        void checkMotionBoolean() {
            // When the car is moving to the centre but a new movement is triggered,
            // Movement to the centre should be terminated.
            if (continueMotionToCentre == true) {
                continueMotionToCentre = false;
            }
        }

    }

    public void centreControl() {
        // Start a thread that will centre the car.
        // If a movement is triggered again, the motion to centre should be cancelled.
        Thread motionToCentreThread = new Thread(new StartMotionToCentre(), "MotionToCentreThread");
        continueMotionToCentre = true;
        motionToCentreThread.start();
    }

    public boolean carInCentre() {
        if (angle == 0 && position.y == topLeftCarY) {
            return true;
        } else {
            return false;
        }
    }

    private void updateTransform() {
        transform.setToIdentity();
        transform.translate(position.x, position.y);
        transform.rotate(orientation, car.getWidth() / 2, car.getHeight() / 2);
        repaint();
    }

    public void rotate(double angle) {
//        System.out.println("Angle: " + angle);
        orientation = (float) Math.toRadians(angle);
//        System.out.println("Orientation: " + orientation);
        updateTransform();

    }

    public void translate(int dx, int dy) {
        position.translate(dx, dy);
        updateTransform();
    }

    private class StartMotionToCentre implements Runnable {

        @Override
        public void run() {
            while (continueMotionToCentre) {
                // Rotate to 0.
                if (angle < 0) {
                    angle += 1;
                    // Angle (maybe) could become bigger than 0, then reset to 0;
                    if (angle > 0) {
                        angle = 0;
                    }
                    rotate(angle);
                }
                if (angle > 0) {
                    angle -= 1;
                    // Angle (maybe) could become less than 0, then reset to 0;
                    if (angle < 0) {
                        angle = 0;
                    }
                    rotate(angle);
                }
                // Move to topLeftCarY
                if (position.y < topLeftCarY) {
                    translate(0, 2);
                    // Position.y could become bigger than topLeftCarY (maybe). Compensate.
                    if (position.y > topLeftCarY) {
                        translate(0, (position.y - topLeftCarY));
                    }

                }
                if (position.y > topLeftCarY) {
                    translate(0, -2);
                    // Position.y could become less than topLeftCarY (maybe). Compensate.
                    if (position.y < topLeftCarY) {
                        translate(0, (topLeftCarY - position.y));
                    }

                }

                if (carInCentre()) {
                    continueMotionToCentre = false;
                }

                try {
                    Thread.sleep(7);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarPlusCar.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(600, 500));
        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(600, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
