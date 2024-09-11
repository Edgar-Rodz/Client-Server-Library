package Handlers;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import static Network.Server.checkoutUsers;
import static Network.Server.users;

public class CheckoutHandler {
    private static MongoClient mongo;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    private static String connectionString = "mongodb+srv://Edgar:Rodriguez@cluster0.u5d8ix6.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DB = "Library";
    private static final String COLLECTION = "Books";

    private static MongoClientSettings settings;

    static {
        try {
            settings = MongoClientSettings.builder()
                    .applyConnectionString(new com.mongodb.ConnectionString(connectionString))
                    .retryWrites(true)
                    .build();
            database = MongoClients.create(settings).getDatabase(DB);
            collection = database.getCollection(COLLECTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to check if a book is available for checkout
    public static String isBookAvailable(String username, String title) {
        checkoutUsers.clear();
        try {
            // Check if the book is available (not checked out)
            Document book = collection.find(Filters.eq("title", title)).first();
            if (book != null) {
                boolean checkedOut = book.getBoolean("checkedout", false);
                if (!checkedOut) {
                    // Update the book to mark it as checked out
                    collection.updateOne(
                            Filters.eq("title", title),
                            new Document("$set", new Document("checkedout", true)
                                    .append("checkedOutBy", username)) // corrected field name
                    );

                    // Update checkoutUsers map
                    checkoutUsers.put(title, true);

                    return "Checkout success";
                } else {
                    // Book is already checked out
                    checkoutUsers.put(title, false);
                    return "Checkout fail";
                }
            } else {
                // Book not found
                return "Book not found";
            }
        } catch (MongoException e) {
            e.printStackTrace();
            return "Checkout error";
        }
    }

    // Method to checkout a book
    public static String checkoutBook(String checkedOutBy,String bookTitle) {
        try {
            // Find the book and update its status to checked out
            Document book = collection.find(Filters.eq("title", bookTitle)).first();
            if (book != null && !book.getBoolean("checkedOut")) {
                collection.updateOne(
                        Filters.eq("title", bookTitle),
                        new Document("$set", new Document("checkedOut", true).append("checkedOutBy", checkedOutBy))
                );
                return "true"; // Checkout successful
            } else {
                return "false"; // Book is not available or already checked out
            }
        } catch (MongoException e) {
            e.printStackTrace();
            return "error";
        }
    }

    // Method to return a book
    public static boolean returnBook(String bookTitle) {
        try {
            // Find the book and update its status to available
            Document book = collection.find(Filters.eq("title", bookTitle)).first();
            if (book != null && book.getBoolean("checkedOut")) {
                collection.updateOne(
                        Filters.eq("title", bookTitle),
                        new Document("$set", new Document("checkedOut", false).append("checkedOutBy", null))
                );
                return true; // Return successful
            } else {
                return false; // Book was not checked out
            }
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }
}
