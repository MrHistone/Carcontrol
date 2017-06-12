package bart.nl.client;

import carcontrol.Defaults.Coordinates;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Bart Jansen
 */
public class ControlConnection {

    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;
    private CarGUI carGUI;
    private String server;
    private int port;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    public boolean isConnected = false;

    public void setConnectParams(String server, int port, CarGUI carGUI) {
        this.carGUI = carGUI;
        this.server = server;
        this.port = port;
    }

    public boolean connect() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
            carGUI.display(server);
        } catch (IOException ec) {
            carGUI.display("Unable to connect to the car: " + ec);
            return false;
        }

        carGUI.display("Connection to the car is accepted.");

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            carGUI.display("There was an exception creating new Input/output Streams: " + eIO);
            return false;
        }
        // creates the Thread to listen from the server 
        new ListenFromServer().start();

        sendCoordinates(new Coordinates(0, 0, 0, "Client controls connected to the car."));
        isConnected = true;

        // success we inform the caller that it worked
        return true;
    }

    public void sendCoordinates(Coordinates co) {
        try {
            sOutput.writeObject(co);
        } catch (IOException e) {
            carGUI.display("The client controls are no longer connected to the car. " + e);
        }
    }

    public void sendMessage(String str) {
        try {
            sOutput.writeObject(str);
        } catch (IOException e) {
            carGUI.display("The client controls are no longer connected to the car. " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        try {
            if (sInput != null) {
                sInput.close();
            }
        } catch (Exception e) {
        }
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }

        carGUI.display("Disconnected.");
    }

    class ListenFromServer extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = (String) sInput.readObject();
                    // Geef het bericht door aan de GUI.
                    String time = sdf.format(new Date()) + " " + msg;
                    carGUI.display(time);
                } catch (IOException e) {
                    carGUI.display("Server has close the connection: " + e);
                    break;
                } catch (ClassNotFoundException ex) {
                    carGUI.display("ClassNotFoundException: " + ex);
                }
            }
        }
    }

}
