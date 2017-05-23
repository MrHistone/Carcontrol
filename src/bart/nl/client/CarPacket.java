package bart.nl.client;

import bart.nl.Defaults.CarAction;

/**
 * Class which is used to send data to the car.
 *
 * @author Bart Jansen
 */
public class CarPacket {

    private int movementX;
    private int movementY;
    private CarAction carAction;

    public void setMovementX(int movementX) {
        this.movementX = movementX;
    }

    public void setMovementY(int movementY) {
        this.movementY = movementY;
    }

    public int getMovementX() {
        return movementX;
    }

    public int getMovementY() {
        return movementY;
    }

    public CarAction getCarAction() {
        return carAction;
    }

    public void setCarAction(CarAction carAction) {
        this.carAction = carAction;
    }

}
