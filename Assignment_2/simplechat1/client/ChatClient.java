// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 


  String clientName; //added for E50
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String clientName, String host, int port, ChatIF clientUI) throws IOException {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;

    // Changed for E50 - HJ
    this.clientName = clientName;
    if(clientName.equals("")){
      this.clientName ="<NO CLIENT ALIAS PROVIDED>";
    }
    openConnection();
    sendToServer(" #clientName " +this.clientName);

  }



  //Instance methods ************************************************

  /***
   * Added for E49 -> connection Exeception handle - HJ
   */

  @Override
  public void connectionException (Exception exp){ //
    System.out.println("Server has shut down, and quitting."); //system message
    quit(); // to terminate the client
  }

  /***
   * Added for E49 -> connectionClosed handle - HJ
   */
  @Override
  public void connectionClosed(){
    System.out.println("The connection has closed."); // system message
  }


  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */

  public void handleMessageFromClientUI(String message) {
    if(message.charAt(0)=='#'){
     try{
       handlingCommand(message);
     }catch(IOException e){
       System.out.println(e);
     }
    }else {

      try {
        sendToServer(message);

      } catch (IOException e) {
        clientUI.display("Could not send message to server.  Terminating client.");
        quit();
      }
    }
  }

  //Added for E49
  private void handlingCommand(String message) throws IOException {
    String[] splitMsg = message.split(" ", 2);
    if (splitMsg[0].charAt(0)=='#') {
      this.clientName = splitMsg[1];
      sendToServer("#clientName" + splitMsg[1]);
    } else {
      throw new IOException("Command is invalid");
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
