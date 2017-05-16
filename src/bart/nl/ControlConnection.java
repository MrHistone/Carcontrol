package bart.nl;

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
        try {
            // Server is aan het luisteren, dit is dan het eerste bericht dat hij doorkrijgt.
            sOutput.writeObject("Eerste bericht wat doorkomt.");
        } catch (IOException eIO) {
            carGUI.display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    void sendMessage(String str) {
        try {
            sOutput.writeObject(str);
        } catch (IOException e) {
            carGUI.display("Exception writing to server: " + e);
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

        public void run() {
            while (true) {
                try {
                    // Programma luistert totdat er iets binnenkomt. Dan gaat hij pas verder.
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
