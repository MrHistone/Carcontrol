package bart.nl;

import java.util.ArrayList;
import java.util.List;
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
    private int speed = 3;

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
                    // Is key still pressed?
                    int y = window.getBoxPanel().getControlY() - speed;
                    window.getBoxPanel().setControlY(y);
                    window.getBoxPanel().repaint();
                } else {

                }

                if (backward == true) {
                    int y = window.getBoxPanel().getControlY() + speed;
                    window.getBoxPanel().setControlY(y);
                    window.getBoxPanel().repaint();
                } else {

                }

                if (right == true) {
                    int x = window.getBoxPanel().getControlX() + speed;
                    window.getBoxPanel().setControlX(x);
                    window.getBoxPanel().repaint();
                } else {

                }

                if (left == true) {
                    int x = window.getBoxPanel().getControlX() - speed;
                    window.getBoxPanel().setControlX(x);
                    window.getBoxPanel().repaint();
                } else {

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
