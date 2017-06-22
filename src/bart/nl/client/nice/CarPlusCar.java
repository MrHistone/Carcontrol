package bart.nl.client.nice;

import carcontrol.Defaults.CarAction;
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
    private CarStatus carStatusCar;

    /**
     * Creates new form CarPlusCar
     */
    public CarPlusCar() {
        initComponents();
        if (!java.beans.Beans.isDesignTime()) {
            car = LoadImage("images//car.png");
            angle = 0;
            carStatusCar = new CarStatus();

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

//            transform.rotate(Math.toRadians(x), car.getWidth() / 2, car.getHeight() / 2);
//            repaint();
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

    public void moveCarAction(CarStatus carStatus) {
        // Compare new movement with old movement.
        if (carStatus.isForward() == true) {
            carStatusCar.setForward(true);
        } else {
            carStatusCar.setForward(false);
        }

        if (carStatus.isBackward()) {
            carStatusCar.setBackward(true);
        } else {
            carStatusCar.setBackward(false);
        }

        if (carStatus.isRight()) {
            carStatusCar.setRight(true);
        } else {
            carStatusCar.setRight(false);
        }

        if (carStatus.isLeft()) {
            carStatusCar.setLeft(true);
        } else {
            carStatusCar.setLeft(false);
        }
    }

    private class MoveCar implements Runnable {

        public boolean keepGoing = true;

        @Override
        public void run() {
            // Continuous scan of the CarStatus...
            while (keepGoing) {
                if (carStatusCar.isBackward()) {

                }

                if (carStatusCar.isForward()) {

                }

                if (carStatusCar.isRight()) {

                }

                if (carStatusCar.isLeft()) {

                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarPlusCar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    public void centreControl() {
//        System.out.println("centre control");
    }

    public boolean carInCentre() {
//        System.out.println(Math.atan2(at.getShearY(), at.getScaleY())); 
//        System.out.println("TranslateY: " + transform.getTranslateY());
//        if (Math.atan2(at.getShearY(), at.getScaleY()) != 0) {
//            return false;
//        }
        return true;
    }

    private void updateTransform() {
        transform.setToIdentity();
        transform.translate(position.x, position.y);
        transform.rotate(orientation, car.getWidth() / 2, car.getHeight() / 2);
        repaint();
    }

    public void rotate(double angle) {
        System.out.println("Angle: " + angle);
        orientation = (float) Math.toRadians(angle);
        System.out.println("Orientation: " + orientation);
        updateTransform();

    }

    public void translate(int dx, int dy) {
        position.translate(dx, dy);
        updateTransform();
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
