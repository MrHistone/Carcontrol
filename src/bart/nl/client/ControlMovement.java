package bart.nl.client;

import bart.nl.client.nice.CarPlusFrame;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

/**
 * This class controls all the movement of the GUI.
 *
 * @author Bart Jansen
 */
public class ControlMovement {

    private CarGUIJPanel carGUIJPanel;
    private CarPlusFrame carPlusFrame;
    private Controller[] controllers;
    private List<Controller> keyboards = new ArrayList<>();
    private boolean forward = false, backward = false, right = false, left = false;
    private final int speed = 3;
    private boolean isMovingToCentre = false, isClockStarted = false;
    private long timeStart;
    private boolean objectAtRest = true;

    private enum GuiType {
        CIRCLE, CAR
    };

    private GuiType guiType;

    public ControlMovement(CarGUI window) {
        carGUIJPanel = window.getBoxPanel();
        carGUIJPanel.centerControl();
        guiType = GuiType.CIRCLE;
        attachController();
    }

    public ControlMovement(CarPlusFrame carPlusFrame) {
        this.carPlusFrame = carPlusFrame;
        guiType = GuiType.CAR;
        attachController();
    }

    public void attachController() {
        controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        if (controllers.length == 0) {
            System.out.println("Found no controllers.");
            System.exit(0);
        }

        // Get all keyboards attached
        for (Controller c : controllers) {
            if (c.getType() == Controller.Type.KEYBOARD) {
                keyboards.add(c);
            }
        }

        Thread threadPollDevices = new Thread(new PollDevices(), "PollDevices");
        threadPollDevices.start();

        Thread threadHandleMovement = new Thread(new HandleMovement(), "HandleMovement");
        threadHandleMovement.start();

    }

    class PollDevices implements Runnable {

        private Component.Identifier.Key key;
        private float value;

        @Override
        public void run() {
            while (true) {
                for (Controller c : keyboards) {
                    c.poll();
                    EventQueue queue = c.getEventQueue();
                    Event event = new Event();
                    while (queue.getNextEvent(event)) {
                        Component comp = event.getComponent();
                        if (!comp.isAnalog()) {
                            key = (Component.Identifier.Key) comp.getIdentifier();
                            value = event.getValue();
                            if (key == Component.Identifier.Key.W || key == Component.Identifier.Key.UP) {
                                if (value == 1.0f) {
                                    forward = true;
                                } else {
                                    forward = false;
                                }
                            }
                            if (key == Component.Identifier.Key.S || key == Component.Identifier.Key.DOWN) {
                                if (value == 1.0f) {
                                    backward = true;
                                } else {
                                    backward = false;
                                }
                            }
                            if (key == Component.Identifier.Key.A || key == Component.Identifier.Key.LEFT) {
                                if (value == 1.0f) {
                                    left = true;
                                } else {
                                    left = false;
                                }
                            }
                            if (key == Component.Identifier.Key.D || key == Component.Identifier.Key.RIGHT) {
                                if (value == 1.0f) {
                                    right = true;
                                } else {
                                    right = false;
                                }
                            }

                        }

                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private class HandleMovement implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (forward == true) {
                    if (isClockStarted || isMovingToCentre) {
                        isClockStarted = false;
                        isMovingToCentre = false;
                    }
                    moveXY(0, speed * -1);

                }

                if (backward == true) {
                    if (isClockStarted || isMovingToCentre) {
                        isClockStarted = false;
                        isMovingToCentre = false;
                    }
                    moveXY(0, speed);
                }

                if (right == true) {
                    if (isClockStarted || isMovingToCentre) {
                        isClockStarted = false;
                        isMovingToCentre = false;
                    }
                    moveXY(speed, 0);
                }

                if (left == true) {
                    if (isClockStarted || isMovingToCentre) {
                        isClockStarted = false;
                        isMovingToCentre = false;
                    }
                    moveXY(speed * -1, 0);
                }

                // If all movement is false and object is not in the resting position, 
                // start moving the object slowly back to the resting position.
                if (isMovingToCentre == false
                        && forward == false
                        && backward == false
                        && right == false
                        && left == false) {
                    objectAtRest = true;
                    switch (guiType) {
                        case CAR:
                            if (carPlusFrame.carInCentre() == false) {
                                objectAtRest = false;
                            }
                            break;
                        case CIRCLE:
                            if (carGUIJPanel.circleInCentre() == false) {
                                objectAtRest = false;
                            }
                            break;
                    }
                }
                if (objectAtRest == false) {
                    // Go to centre after a short period of no action.
                    if (isClockStarted == false) {
                        // Start the clock
                        isClockStarted = true;
                        timeStart = System.currentTimeMillis();
                    } else {
                        if ((System.currentTimeMillis() - timeStart) > 500) {
                            isMovingToCentre = true;
                            centreControl();

                        }
                    }
                }

                // Give it some much needed rest...
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void centreControl() {
        switch (guiType) {
            case CAR:
                carPlusFrame.centreControl();
                break;
            case CIRCLE:
                carGUIJPanel.centerControl();
                break;
        }
    }

    private void moveXY(int X, int Y) {
        switch (guiType) {
            case CAR:
                carPlusFrame.moveXY(X, Y);
                break;
            case CIRCLE:
                carGUIJPanel.moveXY(X, Y);
                break;
        }
    }

}
