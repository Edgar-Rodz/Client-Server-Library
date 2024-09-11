package Handlers;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;

import static Network.Server.wishArray;

public class WishViewHandler {
    private static MongoClient mongo;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    private static String connectionString = "mongodb+srv://Edgar:Rodriguez@cluster0.u5d8ix6.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DB = "Wishlist";
    private static final String COLLECTION = "wish";


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


    public static String viewWish(String username){

        try {
            // Find the user's document
            Document userDocument = collection.find(Filters.eq("username", username)).first();

            // If user document doesn't exist or wishlist is empty, return an empty array
            if (userDocument == null || !userDocument.containsKey("wishlist")) {
                return "wishArray";
            }

            // Get the wishlist from the document
            List<String> wishlist = (List<String>) userDocument.get("wishlist");

            // Convert the wishlist to an array of strings
            wishArray = wishlist.toArray(new String[wishlist.size()]);


            return "wishArray";
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return an empty array if an exception occurs
        }
    }
}
