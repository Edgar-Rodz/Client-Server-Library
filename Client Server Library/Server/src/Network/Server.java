package Network;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;

import Handlers.CheckoutHandler;
import Handlers.CreateAccountHandler;
import Handlers.LoginHandler;
import Handlers.ReturnHandler;
import Handlers.ResetHandler;
import Handlers.WishHandler;
import Handlers.WishViewHandler;

import com.google.gson.Gson;

public class Server extends Observable {
  private PrintWriter toClient;
  public static HashMap<String, String> users = new HashMap<String, String>();
  public static HashMap<String, String> createUsers = new HashMap<String, String>();
  public static HashMap<String, Boolean> checkoutUsers = new HashMap<String, Boolean>();

  public static HashMap<String, String> resetUsers = new HashMap<String, String>();

  public static HashMap<String, String> wishUsers = new HashMap<String, String>();
  public static String[] wishArray = new String[0];


  public static void main(String[] args) {
    new Server().runServer();
  }

  private void runServer() {
    try {
      setUpNetworking();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  private void setUpNetworking() throws Exception {
    @SuppressWarnings("resource")
    ServerSocket serverSock = new ServerSocket(7556);
    while (true) {
      Socket clientSocket = serverSock.accept();
      System.out.println("Connecting to... " + clientSocket);

      ClientHandler handler = new ClientHandler(this, clientSocket);
      this.addObserver(handler);

      Thread t = new Thread(handler);
      t.start();
    }
  }
  public void sendtoClient(String input){

    if(input.contains("Login")){
      Gson gson = new Gson();
      String usersJson = gson.toJson(users); // Convert HashMap to JSON
      toClient.println(usersJson); // Send JSON to client
      toClient.println(); // Send an empty line as a signal for the client to stop reading
      toClient.flush();
      System.out.println("Sent users JSON to client: " + usersJson);
    }
//    toClient.println(input);
//    toClient.flush();
  }

  protected String processRequest(String input) {
    String output = "";
    Gson gson = new Gson();
    try {
      if(input.contains("create")) {
        CreateAccountMessage message = gson.fromJson(input, CreateAccountMessage.class);
        String username =  message.username;
        String password =  message.password;
        output = CreateAccountHandler.insert(username, password);

      }else if(input.contains("Login account")) {
        LoginMessage message = gson.fromJson(input, LoginMessage.class);
        String loginUser = message.username;
        String loginPass = message.password;
        output = LoginHandler.userExists(loginUser, loginPass);

      }else if(input.contains("checkout")){
          CheckoutMessage message = gson.fromJson(input, CheckoutMessage.class);
          String Username = message.getUsername();
          String title = message.getBookTitle();
          checkoutUsers.clear();
          output = CheckoutHandler.isBookAvailable(Username,title);
      }else if(input.contains("return")){
        ReturnMessage message = gson.fromJson(input,ReturnMessage.class);
        String Username = message.getUsername();
        String title = message.getBookTitle();
        checkoutUsers.clear();
        output = ReturnHandler.returnBook(Username,title);
      }else if(input.contains("Reset")){
        ResetMessage message = gson.fromJson(input,ResetMessage.class);
        String Username = message.getUsername();
        String loginPass = message.getPassword();
        output = ResetHandler.resetPass(Username,loginPass);
      }else if(input.contains("wish")){
        WishMessage message = gson.fromJson(input, WishMessage.class);
        String Username = message.getUsername();
        String title = message.getBookTitle();
        output = WishHandler.addWish(Username,title);
      }else if(input.contains("View")){
        WishViewMessage message = gson.fromJson(input, WishViewMessage.class);
        String Username = message.getUsername();
        output = WishViewHandler.viewWish(Username);
      }
      this.setChanged();
      System.out.println(output);
      this.notifyObservers(output);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

}