package bart.nl.client;

public class StartClient {

    private final CarGUI window;
    private final ControlConnection conConn;

    public static void main(String[] args) {
        new StartClient();
    }

    public StartClient() {
        conConn = new ControlConnection();
        window = new CarGUI();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setConConn(conConn);
        new ControlMovement(window);
    }
}
