
import common.ChatIF; 
import java.io.*; 


//added for E50b - HJ + LY

public class ServerConsole implements ChatIF{ 
    final public static int DEFAULT_PORT=5555;
    EchoServer server;

    public ServerConsole(int port){
        this.server = new EchoServer(port);
    }

        // code from ClientConsole but edited for ServerConsole
    public void accept(){
        try{ 
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message; 

            while(true){
                message = fromConsole.readLine();
                if (message.startsWith("#", 0)) {
                	handlingCommand(message);
                } else {
                	System.out.println("Message received: " + message + " from server");
                	display(message);
                }
//                server.handleMessageFromServerUI(message);
                System.out.println("SERVER MSG>" + message);
            }

        }catch(Exception e){
            System.out.println("Unexpected error while reading from console!");
        }  

    }
    
    // handling commands using if-blocks and startsWith
    // added for E50c by HJ + LY
    public void handlingCommand(String message) {
        try {
            if (message.equals("#quit")) {
            	//quits the server and closes the program
            	display("Exiting server.");
            	server.close();
            	System.out.println("Exiting server.");
            	System.exit(0);
            } else if (message.equals("#stop")) {
            	//stops the server from listening for new connections
            	display("Server has stopped listening for new connections.");
            	server.stopListening(); 
            } else if (message.equals("#close")) {
            	//closes the server to all clients
            	display("Server is closed.");
            	server.close();
            } else if (message.startsWith("#setport")) {
            	//sets a new server port
            	// retrieves the port number
                int port = Integer.parseInt(message.substring(8, message.length() - 1).trim());
                display("Server port is now set to: " + port);
                server.setPort(port);
            } else if (message.equals("#start")) {
            	//starts a server listening for connections
            	if (!server.isListening()){
                	display("Server is now listening for new connections.");
                	server.serverStarted();
                } else {
                	System.out.println("Server has already started.");
                }
            } else if (message.equals("#getport")) {
            	//gets the current port
            	display("Server port is: " + server.getPort());
            } else {
            	//default message if illegal command issued
                System.out.println("Valid commands prefaced with '#' are: quit,"
                        + " stop, close, setport <port>, start, and getport.");
            }
        } catch(Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }
    
    // Added by LY
    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message) 
    {
      System.out.println("> " + message);
    }
    
    // code from echo server but edited for server console
    public static void main(String[] args) {
		int port = 0;
		try {
			port=Integer.parseInt(args[0]);
		}

		catch(Throwable thr) {
			port = DEFAULT_PORT;
		}
		
		ServerConsole scan = new ServerConsole(port);
		
        try {
			scan.server.listen();
		}
		catch(Exception e) {
			System.out.println("ERROR - Could not listen for clients!");
		}
		scan.accept();
	}
}