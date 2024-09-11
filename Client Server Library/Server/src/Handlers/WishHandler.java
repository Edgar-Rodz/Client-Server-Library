package Handlers;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Network.Server.createUsers;
import static Network.Server.wishUsers;
import static Network.Server.wishArray;

public class WishHandler {
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

    public static String addWish(String username, String title){
        try {
            // Check if the user already has a wishlist
            Document existingUser = collection.find(Filters.eq("username", username)).first();
            if (existingUser == null) {
                // If user doesn't have a wishlist, create a new one
                List<String> wishlist = new ArrayList<>();
                wishlist.add(title);
                Document newUser = new Document("username", username)
                        .append("wishlist", wishlist);
                collection.insertOne(newUser);
                wishUsers.put("Wish","Success");
                return "Book added to wishlist";
            } else {
                // If user already has a wishlist, check if the book is already in the wishlist
                List<String> wishlist = (List<String>) existingUser.get("wishlist");
                if (wishlist.contains(title)) {
                    wishUsers.put("Book","Exist");
                    return "Book already exists in wishlist";
                } else {
                    // If the book is not in the wishlist, add it
                    wishlist.add(title);
                    collection.updateOne(Filters.eq("username", username),
                            new Document("$set", new Document("wishlist", wishlist)));
                    wishUsers.put("Wish","Success");
                    return "Book added to wishlist";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add book to wishlist";
        }
    }


}

