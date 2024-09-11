package Network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Observer;
import java.util.Observable;

import static Network.Server.*;

class ClientHandler implements Runnable, Observer {

  private Server server;
  private Socket clientSocket;
  private BufferedReader fromClient;
  private PrintWriter toClient;

  protected ClientHandler(Server server, Socket clientSocket) {
    this.server = server;
    this.clientSocket = clientSocket;
    try {
      fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
      toClient = new PrintWriter(this.clientSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void sendToClient(String string) {
    if(string.contains("Login")){
      Gson gson = new Gson();
      String usersJson = gson.toJson(users); // Convert HashMap to JSON
      toClient.println("Login:"+usersJson); // Send JSON to client
      toClient.flush();
      System.out.println("Sent users JSON to client: " + usersJson);
    }else if(string.contains("Create")){
      Gson gson = new Gson();
      String createJson = gson.toJson(createUsers); // Convert HashMap to JSON
      toClient.println("Create:"+createJson); // Send JSON to client
      toClient.flush();
      System.out.println("Sent users JSON to client: " + createJson);
    }else if(string.contains("Checkout")){
      Gson gson = new Gson();
      String createJson = gson.toJson(checkoutUsers); // Convert HashMap to JSON
      toClient.println("Checkout:"+createJson); // Send JSON to client
      toClient.flush();
    }else if(string.contains("Return")){
      Gson gson = new Gson();
      String createJson = gson.toJson(checkoutUsers); // Convert HashMap to JSON
      toClient.println("Return:"+createJson); // Send JSON to client
      toClient.flush();
    }else if(string.contains("reset")) {
      Gson gson = new Gson();
      String createJson = gson.toJson(resetUsers); // Convert HashMap to JSON
      toClient.println("Reset:" + createJson); // Send JSON to client
      toClient.flush();
    } else if(string.contains("wishlist")) {
      Gson gson = new Gson();
      String createJson = gson.toJson(wishUsers); // Convert HashMap to JSON
      toClient.println("Wish:" + createJson); // Send JSON to client
      toClient.flush();
    }else if(string.contains("wishArray")) {
      Gson gson = new Gson();
      String createJson = gson.toJson(wishArray); // Convert HashMap to JSON
      toClient.println("View3:" + createJson); // Send JSON to client
      toClient.flush();
    }
  }

  @Override
  public void run() {
    String input;
    try {
      while ((input = fromClient.readLine()) != null) {
        System.out.println("From client: " + input);
        server.processRequest(input);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    this.sendToClient((String) arg);
  }
}