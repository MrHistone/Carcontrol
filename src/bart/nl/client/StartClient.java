package bart.nl.client;

import bart.nl.client.nice.CarPlusFrame;

public class StartClient {

//    private final CarGUI window;
//    private final ControlConnection conConn;
    private final CarPlusFrame carPlusFrame;

    public static void main(String[] args) {
        new StartClient();
    }

    public StartClient() {
//        conConn = new ControlConnection();
//        window = new CarGUI();
//        window.setLocationRelativeTo(null);
//        window.setVisible(true);
//        window.setConConn(conConn);
//        new ControlMovement(window);
        
        carPlusFrame = new CarPlusFrame();
        carPlusFrame.setVisible(true);
        carPlusFrame.setLocationRelativeTo(null);
        new ControlMovement(carPlusFrame);
        
    }
}
