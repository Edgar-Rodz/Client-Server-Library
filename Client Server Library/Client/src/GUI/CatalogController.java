package GUI;

import Network.Messages.CheckoutMessage;
import Network.Messages.ReturnMessage;
import Network.Messages.ViewWishMessage;
import Network.Messages.WishMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static GUI.Client.*;


public class CatalogController implements Initializable {
    private static String host = "localhost";
    private  BufferedReader fromServer;
    private  PrintWriter toServer;
    @FXML
    private Label Output;


    @FXML private TableView<Book> catalogTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button Reset;
    @FXML private Button view;
    @FXML private Label viewList;


    @FXML private Button wishlist;

    private FilteredList<Book> filteredData;

    Object lock = new Object();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize columns
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        addTestBooks();
        filteredData = new FilteredList<>(catalogTable.getItems(), p -> true);

        // Bind the filtered list to the table view
        catalogTable.setItems(filteredData);

    }

    // Method to add test books to the catalog
    private void addTestBooks() {
        // Add some test books
        catalogTable.getItems().add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "9780743273565", 180));
        catalogTable.getItems().add(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", "9780061120084", 324));
        catalogTable.getItems().add(new Book("1984", "George Orwell", "Science Fiction", "9780451524935", 328));
        catalogTable.getItems().add(new Book("Pride and Prejudice", "Jane Austen", "Romance", "9780141439518", 279));
        catalogTable.getItems().add(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", "9780316769488", 277));
        catalogTable.getItems().add(new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Fantasy", "9780590353427", 320));
        catalogTable.getItems().add(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", "9780345339683", 310));


    }

    public void UserLogout(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientGui.fxml"));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.centerOnScreen();
        stage.setTitle("Create Account");
        stage.show();

    }

    public void addToWish(){
        Book selectedBook = catalogTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Get the username (you may need to adjust how you obtain the username based on your application)
            String username = UserData.getInstance().getUsername(); // For example, replace "edgar" with the actual username
            WishMessage wishMessage = new WishMessage(username, selectedBook.getTitle());
            Gson gson = new Gson();
            Client.sendToServer(gson.toJson(wishMessage));
        }
        while (wishMap == null) {
            try {
                Thread.sleep(100); // Wait for 100 milliseconds before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(wishMap.containsValue("Success")){
            Output.setText("The book has been successfully added to your wishlist.");
            Output.setTextFill(Color.GREEN);
        }else if(wishMap.containsValue("Exist")){
            Output.setText("The book has already been added.");
            Output.setTextFill(Color.RED);
        }

    }

    public void viewWish(){
        String username = UserData.getInstance().getUsername();
        ViewWishMessage message = new ViewWishMessage(username);
        Gson gson = new Gson();
        Client.sendToServer(gson.toJson(message));
        while(receivedArray == null){
            try {
                Thread.sleep(100); // Wait for 100 milliseconds before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(receivedArray.length == 0){
            viewList.setText("You do not have a wishlist");
            viewList.setTextFill(Color.RED);
        }else {
            StringBuilder wishlistText = new StringBuilder();
            boolean firstItem = true;
            for (String item : receivedArray) {
                if (item != null) {
                    if (!firstItem) {
                        wishlistText.append(", "); // Add comma and space if it's not the first item
                    } else {
                        firstItem = false;
                    }
                    wishlistText.append(item);
                }
            }
            viewList.setText(wishlistText.toString());
            viewList.setTextFill(Color.GREEN);
        }
    }

    public void resetLog(){
        resetSearch(); // Reset the search
        catalogTable.getSelectionModel().clearSelection();
    }
    private void resetSearch() {
        searchField.clear(); // Clear the search field
        filteredData.setPredicate(null); // Reset the predicate to show all items
    }

    public void searchBooks() {
        String searchText = searchField.getText().toLowerCase();

        Predicate<Book> predicate = book -> {
            String title = book.getTitle().toLowerCase();
            String author = book.getAuthor().toLowerCase();
            return title.contains(searchText) || author.contains(searchText);
        };

        filteredData.setPredicate(predicate);
    }

    public void checkoutBook() throws Exception {
        synchronized (lock) {
            // Get the selected book
            Book selectedBook = catalogTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                // Get the username (you may need to adjust how you obtain the username based on your application)
                String username = UserData.getInstance().getUsername(); // For example, replace "edgar" with the actual username
                CheckoutMessage checkoutMessage = new CheckoutMessage(username, selectedBook.getTitle());
                Gson gson = new Gson();
                Client.sendToServer(gson.toJson(checkoutMessage));
            }
            while (checkoutMap == null) {
                try {
                    Thread.sleep(100); // Wait for 100 milliseconds before checking again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (checkoutMap.containsKey(selectedBook.getTitle())) {
                String storedBool = checkoutMap.get(selectedBook.getTitle());

                    if (storedBool.equals("true")) {

                            //display checkout success
                            Output.setText("The book has been successfully checked out.");
                            Output.setTextFill(Color.GREEN);

                    } else {

                            Output.setText("Womp Womp This book cannot be checked out");
                            Output.setTextFill(Color.RED);

                    }

            }
//            checkoutMap.clear();
        }
    }

    public void  returnBook() throws Exception {
        synchronized (lock) {
            Book selectedBook = catalogTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                // Get the username (you may need to adjust how you obtain the username based on your application)
                String username = UserData.getInstance().getUsername(); // For example, replace "edgar" with the actual username
                ReturnMessage returnMessage = new ReturnMessage(username, selectedBook.getTitle());
                Gson gson = new Gson();
                Client.sendToServer(gson.toJson(returnMessage));
            }
            while (returnMap == null) {
                try {
                    Thread.sleep(100); // Wait for 100 milliseconds before checking again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(returnMap.containsKey("USER")){
                Platform.runLater(() -> {
                    Output.setText("YOU SHALL NOT PASS! You cannot return this book");
                    Output.setTextFill(Color.RED); // Set text color to red
                });
                returnMap.clear();
            }
            else if (returnMap.containsKey(selectedBook.getTitle())) {
                String storedBool = returnMap.get(selectedBook.getTitle());


                if (storedBool.equals("false")) {
                    //display checkout success
                    Platform.runLater(() -> {
                        Output.setText("The book has been successfully returned successfully.");
                        Output.setTextFill(Color.GREEN);
                        returnMap.clear();
                    });

//                    returnMap.remove(selectedBook.getTitle(),storedBool);
                } else {
                    Platform.runLater(() -> {
                        Output.setText("Please Try Again!");
                        Output.setTextFill(Color.PURPLE);
                        returnMap.clear();
                    });
                }
            }
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
