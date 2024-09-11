package Handlers;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;

import org.bson.Document;

import static Network.Server.users;
import static Network.Server.createUsers;

public class CreateAccountHandler {

    private static MongoClient mongo;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    private static String connectionString = "mongodb+srv://Edgar:Rodriguez@cluster0.u5d8ix6.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DB = "Login";
    private static final String COLLECTION = "Username";

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
    public static String insert(String username, String password) {
        createUsers.clear();
        try {
            Document existingUser = collection.find(Filters.eq("username", username)).first();
            if (existingUser != null) {
                createUsers.put("Create","Fail");
                return "Create";
            }

            Document doc = new Document("username", username)
                    .append("password", password);
            collection.insertOne(doc);
            createUsers.put("Create","Success");
            return "Create"; // Successfully inserted
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception error"; // Failed to insert
        }
    }

    public static boolean validateLogin(String username, String password) {
        try {
            // Search for the user in the database
            Document query = collection.find(Filters.eq("username", username)).first();

            // If the user is found and the password matches, return true
            if (query != null && query.getString("password").equals(password)) {
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        // If user is not found or password does not match, return false
        return false;
    }

    public static boolean userExists(String username) {
        try {
            // Search for the user in the database
            Document query = collection.find(Filters.eq("username", username)).first();

            // If the user is found, return true
            return query != null;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }
}
