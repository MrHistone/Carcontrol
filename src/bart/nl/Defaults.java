package bart.nl;

import java.io.Serializable;

public class Defaults {

    public enum Momentum {
        FORWARD, BACKWARD, RIGHT, LEFT, STOP
    }

    public enum CarAction {
        FORWARD, BACKWARD, RIGHT, LEFT, STOP, HORN
    }

    /**
     * Class used to send coordinates to the connected car.
     */
    public static class Coordinates implements Serializable {

        private int xCoordinate = 0;
        private int yCoordinate = 0;
        private double maxMovement = 0;
        private double xPercentage = 0;
        private double yPercentage = 0;
        private String message;
        private String xCoordinateString;
        private String yCoordinateString;

        /**
         * Constructor used to create a new instance with all necessary instance variables.
         * @param xCoordinate int X Coordinate
         * @param yCoordinate int Y Coordinate
         * @param maxMovement double Maximum movement
         * @param message String Message
         */
        public Coordinates(int xCoordinate, int yCoordinate, double maxMovement, String message) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
            this.maxMovement = maxMovement;
            this.message = message;
        }

        /**
         * Default constructor.
         */
        public Coordinates() {

        }

        public int getXCoordinate() {
            return xCoordinate;
        }

        public int getYCoordinate() {
            return yCoordinate;
        }

        public double getMaxMovement() {
            return maxMovement;
        }

        public double getXPercentage() {
            if (maxMovement > 0) {
                xPercentage = xCoordinate / (maxMovement / 100);
            } else {
                xPercentage = 0;
            }
            return xPercentage;
        }

        public double getYPercentage() {
            if (maxMovement > 0) {
                yPercentage = yCoordinate / (maxMovement / 100);
            } else {
                yPercentage = 0;
            }
            return yPercentage;
        }

        public void setXCoordinate(int xCoordinate) {
            this.xCoordinate = xCoordinate;
        }

        public void setYCoordinate(int yCoordinate) {
            this.yCoordinate = yCoordinate;
        }

        public void setMaxMovement(int maxMovement) {
            this.maxMovement = maxMovement;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getxCoordinateString() {
            return xCoordinateString;
        }

        public void setxCoordinateString(String xCoordinateString) {
            this.xCoordinateString = xCoordinateString;
        }

        public String getyCoordinateString() {
            return yCoordinateString;
        }

        public void setyCoordinateString(String yCoordinateString) {
            this.yCoordinateString = yCoordinateString;
        }

    }

}
