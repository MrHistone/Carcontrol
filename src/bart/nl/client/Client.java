package bart.nl.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Client {

    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;
    private ClientGUI clientGUI;
    private String server;
    private int port;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");;

    Client(String server, int port, ClientGUI clientGUI) {
        // which calls the common constructor with the GUI set to null
        this.server = server;
        this.port = port;        
        this.clientGUI = clientGUI;
    }

    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromServer().start();
        try {
            // Server is aan het luisteren, dit is dan het eerste bericht dat hij doorkrijgt.
            sOutput.writeObject("Eerste bericht wat doorkomt.");
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    private void display(String msg) {
        System.out.println(msg);
    }

    void sendMessage(String str) {
        try {
            sOutput.writeObject(str);
        } catch (IOException e) {
            display("Exception writing to server: " + e);
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
        } // not much else I can do
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        } // not much else I can do

        // inform the GUI
        if (clientGUI != null) {
        }

    }

    class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {
                    // Programma luistert totdat er iets binnenkomt. Dan gaat hij pas verder.
                    String msg = (String) sInput.readObject();
                    // if console mode print the message and add back the prompt
                    if (clientGUI == null) {
                        System.out.println(msg);
                        System.out.print("> ");
                    } else {
                        // Geef het bericht door aan de GUI.
                        String time = sdf.format(new Date()) + " " + msg;
                        clientGUI.showServerMessage(time);
                    }
                } catch (IOException e) {
                    display("Server has close the connection: " + e);
                    if (clientGUI != null) {
                    }
                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                }
            }
        }
    }
}
