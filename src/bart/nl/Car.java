package bart.nl;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import bart.nl.Defaults.CarAction;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Car {

    private SimpleDateFormat sdf;
    private GpioController gpio;
    final GpioPinDigitalOutput pin_0;
    final GpioPinDigitalOutput pin_1;
    final GpioPinDigitalOutput pin_2;
    final GpioPinDigitalOutput pin_3;

    public Car() {
        sdf = new SimpleDateFormat("HH:mm:ss");
        // create gpio controller
        gpio = GpioFactory.getInstance();

        // provision gpio pins output pins
        pin_0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED0", PinState.LOW);
        pin_1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "LED1", PinState.LOW);
        pin_2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LED2", PinState.LOW);
        pin_3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "LED3", PinState.LOW);

        // set shutdown state for the pins
        pin_0.setShutdownOptions(true, PinState.LOW);
        pin_1.setShutdownOptions(true, PinState.LOW);
        pin_2.setShutdownOptions(true, PinState.LOW);
        pin_3.setShutdownOptions(true, PinState.LOW);
    }

    protected void moveCar(CarAction carAction, int delay) {
        // Move until stopped
        // Set delay

        switch (carAction) {
            case FORWARD:
                display("Forward.");
                pin_0.setState(PinState.HIGH);
                pin_3.setState(PinState.HIGH);
                break;
            case BACKWARD:
                display("Backward.");
                pin_1.setState(PinState.HIGH);
                pin_2.setState(PinState.HIGH);
                break;
            case LEFT:
                display("Left.");
                pin_1.setState(PinState.HIGH);
                pin_3.setState(PinState.HIGH);
                break;
            case RIGHT:
                display("Right.");
                pin_0.setState(PinState.HIGH);
                pin_2.setState(PinState.HIGH);
                break;
            case STOP:
                display("Stop.");
                pin_0.setState(PinState.LOW);
                pin_1.setState(PinState.LOW);
                pin_2.setState(PinState.LOW);
                pin_3.setState(PinState.LOW);
                break;
        }

    }

    private void display(String msg) {
        String time = sdf.format(new Date()) + " Car: " + msg;
        System.out.println(time);
    }
}
