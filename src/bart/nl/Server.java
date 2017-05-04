package bart.nl;

import bart.nl.Defaults.CarAction;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {

    // to display time
    private SimpleDateFormat sdf;
    // the port number to listen for connection
    private int port;
    // the boolean that will be turned of to stop the server
    private boolean keepGoing;
    private boolean forward = false;
    private CarAction carAction;
    private Car car;

    public Server(int port) {
        // the port
        this.port = port;
        // to display hh:mm:ss
        sdf = new SimpleDateFormat("HH:mm:ss");
        car = new Car();
        
    }

    public void start() {
        keepGoing = true;
        /* create socket server and wait for connection requests */
        try {
            // the socket used by the server
            ServerSocket serverSocket = new ServerSocket(port);

            // infinite loop to wait for connections
            while (keepGoing) {
                // format message saying we are waiting
                display("Server waiting for Clients on port " + port + ".");
                Socket socket = serverSocket.accept();  	// accept connection

                // if I was asked to stop
                if (!keepGoing) {
                    break;
                }
                ClientThread t = new ClientThread(socket);  // make a thread of it
                t.start();
            }
            // I was asked to stop
            try {
                serverSocket.close();
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        } // something went bad
        catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }

    /*
     * For the GUI to stop the server
     */
    protected void stop() {
        keepGoing = false;
        try {
            new Socket("localhost", port);
        } catch (Exception e) {

        }
    }

    private void determineCarAction(String clientMessage) {
        switch (clientMessage) {
            case "RIGHT":
                carAction = carAction.RIGHT;
                break;
            case "LEFT":
                carAction = carAction.LEFT;
                break;
            case "FORWARD":
                carAction = carAction.FORWARD;
                break;
            case "BACKWARD":
                carAction = carAction.BACKWARD;
                break;
            case "STOP":
                carAction = carAction.STOP;
                break;
            case "HORN":
                carAction = carAction.HORN;
                break;
            default:
                carAction = CarAction.STOP;
                
        }
    }
    
    private void initiateCarAction(){
        switch (carAction){
            case BACKWARD:
                car.moveCar(carAction, 100);
                break;
            case FORWARD:
                car.moveCar(carAction, 100);
                break;
            case LEFT:
                car.moveCar(carAction, 100);
                break;
            case RIGHT:
                car.moveCar(carAction, 100);
                break;
            case HORN:
                break;
            case STOP:
                car.moveCar(carAction, 100);
                break;
        }
    }
    

    private void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;
        System.out.println(time);
    }

    public static void main(String[] args) {
        int portNumber = 1500;
        // create a server object and start it
        Server server = new Server(portNumber);
        server.start();
    }

    class ClientThread extends Thread {
        // the socket where to listen/talk

        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        // my unique id (easier for deconnection)
        int id;
        String strMsg;

        // the date I connect
        String date;

        // Constructor
        ClientThread(Socket socket) {
            this.socket = socket;
            /* Creating both Data Stream */
            System.out.println("Thread trying to create Object Input/Output Streams");
            try {
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                // read the username
                display("connected.");
            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                return;
            }
            date = new Date().toString() + "\n";
        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a String (which is an object)
                try {
                    strMsg = (String) sInput.readObject();
                    display(strMsg);
                    writeMsg(strMsg);
                    determineCarAction(strMsg);
                    initiateCarAction();
                    
                } catch (IOException e) {
                    display("Disconnected...: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }
            }
            close();
        }

        // try to close everything
        private void close() {
            // try to close the connection
            try {
                if (sOutput != null) {
                    sOutput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (sInput != null) {
                    sInput.close();
                }
            } catch (Exception e) {
            };
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }

        /*
         * Write a String to the Client output stream
         */
        private boolean writeMsg(String msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeObject(msg);
            } // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message");
                display(e.toString());
            }
            return true;
        }

    }
}
