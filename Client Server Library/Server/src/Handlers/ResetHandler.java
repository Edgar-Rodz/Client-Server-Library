package Handlers;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import static Network.Server.resetUsers;
import static Network.Server.users;

public class ResetHandler {
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


    public static String resetPass(String username, String password){
        resetUsers.clear();
        Document user = collection.find(Filters.eq("username", username)).first();
        if(user != null){
            collection.updateOne(Filters.eq("username", username), Updates.set("password", password));
            resetUsers.put("Reset","Success");
        }else{
            resetUsers.put("Reset","Username");
        }
        return "reset";

    }
}
