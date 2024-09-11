package Handlers;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import static Network.Server.checkoutUsers;

public class ReturnHandler {
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


    public static String returnBook(String username, String title){
        checkoutUsers.clear();
        try {
            // Check if the book is available (not checked out)
            Document book = collection.find(Filters.eq("title", title)).first();
            String checkedOutBy = book.getString("checkedOutBy");
//            System.out.println(checkedOutBy +" " +username);
            if(checkedOutBy.equals(username)) {
                if (book != null) {
                    boolean checkedOut = book.getBoolean("checkedout", false);
                    if (checkedOut) {
                        // Update the book to mark it as checked out
                        collection.updateOne(
                                Filters.eq("title", title),
                                new Document("$set", new Document("checkedout", false)
                                        .append("checkedOutBy", null))
                        );

                        // Update checkoutUsers map
                        checkoutUsers.put(title, false);

                        return "Return success";
                    } else {

                        checkoutUsers.put(title, false);
                        return "Return fail";
                    }
                } else {
                    // Book not found
                    return "Book not found";
                }
            }else{
                checkoutUsers.put("USER",false);
                return "Return Wrong";
            }
        } catch (MongoException e) {
            e.printStackTrace();
            return "Checkout error";
        }
    }
}
