package bart.nl;

public class StartClient {

    private final CarGUI window;

    public static void main(String[] args) {
        new StartClient();

    }

    public StartClient() {
        window = new CarGUI();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        new ControlMovement(window);
    }

}
