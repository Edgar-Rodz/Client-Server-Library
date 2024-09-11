package GUI;

import LibraryUser.Users;
import Network.Messages.LoginMessage;
import Network.Messages.ResetMessage;
import Network.Messages.ReturnMessage;
import com.google.gson.reflect.TypeToken;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import Network.Messages.CreateAccountMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class Client extends Application {

  private static String host = "localhost";
  public static BufferedReader fromServer;
  private static PrintWriter toServer;
  @FXML
  private TextField Username;

  @FXML
  private PasswordField Password;

  static boolean flag = false;
  static boolean received = false;
  private static HashMap<String, String> usersMap;

  public static HashMap<String, String> checkoutMap;
  public static HashMap<String, String> returnMap;
  public static HashMap<String, String> createMap;
  public static HashMap<String, String> resetMap;
  public static HashMap<String, String> wishMap;
  public static String[] receivedArray;
  private static final String SUCCESS_SOUND_FILE = "C:/Users/Edgar Rodz/Downloads/hooray.mp3";
  private static final String GIF_PATH = "C:/Users/Edgar Rodz/Downloads/giphy.gif";

  private static final String VIDEO_FILE = "C:/Users/Edgar Rodz/Downloads/Library.mp4";
  Object lock  = new Object();
  @FXML
  private Label Output;
  @FXML private Circle character;
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientGUI.fxml"));
    Parent root = loader.load();
    Image gifImage = new Image("file:" + GIF_PATH);
    ImageView gifView = new ImageView(gifImage);

    // Set position and size of the ImageView
    gifView.setX(0);
    gifView.setY(0);
    gifView.setFitWidth(60); // Adjust width as needed
    gifView.setFitHeight(40); // Adjust height as needed

    // Add ImageView to the root
    ((Pane) root).getChildren().add(gifView);
    Media media = new Media(new File(VIDEO_FILE).toURI().toString());
    // Create a MediaPlayer to play the video
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    // Create a MediaView to display the video
    MediaView mediaView = new MediaView(mediaPlayer);
    // Set the size of the MediaView
    mediaView.setFitWidth(100);
    mediaView.setFitHeight(100);
    // Set position of the MediaView for the video
    mediaView.setLayoutX(0);
    mediaView.setLayoutY(40);
    // Add MediaView for the video to the root
    ((Pane) root).getChildren().add(mediaView);
    primaryStage.setTitle("JavaFX Application");
    primaryStage.setScene(new Scene(root, 600, 400));
    primaryStage.show();
    mediaPlayer.play();
    setUpNetworking();

  }


  public static void setUpNetworking() throws Exception {
    Socket socket = new Socket(host, 7556);
    System.out.println("Connecting to... " + socket);
    fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    toServer = new PrintWriter(socket.getOutputStream());
    Thread readerThread = new Thread(new Runnable() {
      @Override
      public void run() {
        String input;
        try {
          while ((input = fromServer.readLine()) != null) {
//            System.out.println(input);
            System.out.println("From server: " + input);
            if(input.contains("Login")) {
              Gson gson = new Gson();
              Type type = new TypeToken<HashMap<String, String>>() {}.getType();
              usersMap = gson.fromJson(input.substring(input.indexOf("{")), type);
            }else if(input.contains("Create")){
              Gson gson = new Gson();
              Type type = new TypeToken<HashMap<String, String>>() {}.getType();
              createMap = gson.fromJson(input.substring(input.indexOf("{")), type);
            } else if(input.contains("Checkout")){
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, String>>() {}.getType();
                checkoutMap = gson.fromJson(input.substring(input.indexOf("{")), type);
            }else if(input.contains("Return")){
              Gson gson = new Gson();
              Type type = new TypeToken<HashMap<String, String>>() {}.getType();
              returnMap = gson.fromJson(input.substring(input.indexOf("{")), type);
            }else if(input.contains("Reset")){
              Gson gson = new Gson();
              Type type = new TypeToken<HashMap<String, String>>() {}.getType();
              resetMap = gson.fromJson(input.substring(input.indexOf("{")), type);
            }else if(input.contains("Wish")){
              Gson gson = new Gson();
              Type type = new TypeToken<HashMap<String, String>>() {}.getType();
              wishMap = gson.fromJson(input.substring(input.indexOf("{")), type);
            }else if(input.contains("View")){
              Gson gson = new Gson();
              receivedArray = gson.fromJson(input.substring(input.indexOf(":") + 1), String[].class);

            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    readerThread.start();

  }


  @FXML
  public void UserLogin(ActionEvent event) throws IOException {
    String enteredUsername = Username.getText();
    String enteredPassword = Password.getText();
    String encrypt = encryptPassword(enteredPassword);
    UserData.getInstance().setUsername(enteredUsername);
    LoginMessage login = new LoginMessage(enteredUsername, encrypt);
    Gson gson = new Gson();
    sendToServer(gson.toJson(login));
    Platform.runLater(() ->{
      while (usersMap == null) {
        try {
          Thread.sleep(100); // Wait for 100 milliseconds before checking again
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      if(usersMap.containsValue("Success")){
        try {
          changeScene(event);
          playSound(SUCCESS_SOUND_FILE);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }else if(usersMap.containsValue("Invalid")){
        Username.clear();
        Password.clear();
        Username.setText("Invalid Password");
      }else{
        Username.clear();
        Password.clear();
        Username.setText("Username does not exist");
      }

    });
  }
  @FXML
  public void UserRegister(ActionEvent event) throws IOException {
      String enteredUsername = Username.getText();
      String enteredPassword = Password.getText();
      UserData.getInstance().setUsername(enteredUsername);
    if(enteredPassword.length() < 5){
      Username.clear();
      Password.clear();
      Output.setText("Password is too short.");
      Output.setTextFill(Color.PURPLE);
      return;
    }else if(enteredPassword.contains(" ")){
      Username.clear();
      Password.clear();
      Output.setText("Password cannot contain spaces");
      Output.setTextFill(Color.PURPLE);
      return;
    }
      String encryptPass = encryptPassword(enteredPassword);
      CreateAccountMessage register = new CreateAccountMessage(enteredUsername, encryptPass);
      Gson gson = new Gson();
      sendToServer(gson.toJson(register));
      while (createMap == null) {
        try {
          Thread.sleep(200); // Wait for 100 milliseconds before checking again
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println(createMap);
      if (createMap.containsValue("Success")) {

        try {
          changeScene(event);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else if(createMap.containsValue("Fail")) {
        Username.clear();
        Password.clear();
        Username.setText("Username already exists");
      }

  }
   public void resetPassword(){
     String enteredUsername = Username.getText();
     String enteredPassword = Password.getText();
     String encrypt = encryptPassword(enteredPassword);
     if(enteredPassword.length() < 5){
       Username.clear();
       Password.clear();
       Output.setText("Password is too short.");
       Output.setTextFill(Color.PURPLE);
       return;
     }else if(enteredPassword.contains(" ")){
       Username.clear();
       Password.clear();
       Output.setText("Password cannot contain spaces");
       Output.setTextFill(Color.PURPLE);
       return;
     }
     ResetMessage reset = new ResetMessage(enteredUsername,encrypt);
     Gson gson = new Gson();
     sendToServer(gson.toJson(reset));
     System.out.println("Hi");
     while (resetMap == null) {
       try {
         Thread.sleep(100); // Wait for 100 milliseconds before checking again
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }
    System.out.println(resetMap);
     if(resetMap.containsValue("Success")){
       Platform.runLater(() -> {
         Output.setText("Password Reset was successful");
         Output.setTextFill(Color.GREEN); // Set text color to red
       });

     }else if(resetMap.containsValue("Username")){
       Output.setText("Username does not exist");
       Output.setTextFill(Color.RED);
     }


   }

  public static String encryptPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = md.digest(password.getBytes());
      StringBuilder hexString = new StringBuilder();
      for (byte hashByte : hashBytes) {
        String hex = Integer.toHexString(0xff & hashByte);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }
  @FXML
  public void changeScene(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("CatalogGui.fxml"));
    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(loader.load()));
    stage.centerOnScreen();
    stage.setTitle("Create Account");
    stage.show();

  }

  public static void sendToServer(String input) {


    toServer.println(input);
    toServer.flush();
  }
  private void playSound(String soundFile) {
    try {
      Media sound = new Media(new File(soundFile).toURI().toString());
      MediaPlayer mediaPlayer = new MediaPlayer(sound);
      mediaPlayer.play();
    } catch (Exception e) {
      System.err.println("Error playing sound: " + e.getMessage());
    }
  }


  public void exitOut(){
    System.exit(0);
  }
}