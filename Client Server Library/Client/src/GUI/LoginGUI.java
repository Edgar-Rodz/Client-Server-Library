package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginGUI extends Application {

    public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientGUI.fxml"));
    Parent root = loader.load();
    primaryStage.setTitle("JavaFX Application");
    primaryStage.setScene(new Scene(root, 600, 400));
    primaryStage.show();


  }
    public static void main(String[] args) {
        launch(args);
    }

}
