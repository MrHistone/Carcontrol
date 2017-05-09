package bart.nl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class StartClient {

    private final CarGUI window;
    private CarGUIJPanel carGUIJPanel;
    private Controller[] controllers;
    private List<Controller> keyboards = new ArrayList<>();
    private boolean forward = false, backward = false, right = false, left = false;
    private final int speed = 3;
    private Timer timer;
    private boolean isMovingToCentre = false, isClockStarted = false;
    private long timeStart, timeNow;

    public static void main(String[] args) {
        new StartClient();

    }

    public StartClient() {
        window = new CarGUI();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        carGUIJPanel = window.getBoxPanel();
        carGUIJPanel.centerControl();
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
                            if (key == Component.Identifier.Key.W) {
                                if (value == 1.0f) {
                                    forward = true;
                                } else {
                                    forward = false;
                                }
                            }
                            if (key == Component.Identifier.Key.S) {
                                if (value == 1.0f) {
                                    backward = true;
                                } else {
                                    backward = false;
                                }
                            }
                            if (key == Component.Identifier.Key.A) {
                                if (value == 1.0f) {
                                    left = true;
                                } else {
                                    left = false;
                                }
                            }
                            if (key == Component.Identifier.Key.D) {
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
                    window.getBoxPanel().moveXY(0, speed * -1);
                }

                if (backward == true) {
                    if (isClockStarted || isMovingToCentre) {
                        isClockStarted = false;
                        isMovingToCentre = false;
                    }
                    window.getBoxPanel().moveXY(0, speed);
                } 

                if (right == true) {
                    if (isClockStarted || isMovingToCentre) {
                        isClockStarted = false;
                        isMovingToCentre = false;
                    }
                    window.getBoxPanel().moveXY(speed, 0);
                }

                if (left == true) {
                    if (isClockStarted || isMovingToCentre) {
                        isClockStarted = false;
                        isMovingToCentre = false;
                    }
                    window.getBoxPanel().moveXY(speed * -1, 0);
                } 

                // If all movement is false and circle is not in the middle, 
                // start moving the circle slowly back to the middle.
                if (isMovingToCentre == false
                        && forward == false
                        && backward == false
                        && right == false
                        && left == false
                        && window.getBoxPanel().circleInCentre() == false) {

                    // Go to centre after a short period of no action.
                    if (isClockStarted == false) {
                        // Start the clock
                        isClockStarted = true;
                        timeStart = System.currentTimeMillis();
                    } else {
                        if ((System.currentTimeMillis() - timeStart) > 500) {
                            isMovingToCentre = true;
                            window.getBoxPanel().centerControl();

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

}
