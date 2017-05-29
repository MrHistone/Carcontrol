package bart.nl.server;

/**
 * Start Server with commandline:
 * java -cp carcontrol.jar bart.nl.server.StartServer 1
 * The parameter is to test without a RaspBerry PI attached.
 * @author Bart Jansen
 */
public class StartServer {
    Server server;
    
    public static void main(String[] args){
        // With an argument means that there is no car so that we can test without the Raspberry PI attached.
        
        if (args.length > 0){
            // No car
            new StartServer(false);
            
        } else {
            // With a car
            new StartServer(true);
        }
    }
    
    public StartServer(boolean carAvailable){
        int portNumber = 1500;
        server = new Server(portNumber,carAvailable);
        server.start();
    }
    
        
}
