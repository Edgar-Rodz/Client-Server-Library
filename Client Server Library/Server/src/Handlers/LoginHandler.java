package Handlers;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import static Network.Server.users;

public class LoginHandler {
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

    public static String userExists(String username,String password) {

            users.clear();
        try {
            // Search for the user in the database
            Document user = collection.find(Filters.eq("username", username)).first();
            Document pass = collection.find(Filters.eq("password", password)).first();
            if(pass != null && user != null){
                users.put("Login","Success");
                return "Login: Success";
            }
            else if(pass == null && user != null){
                users.put("Login","Invalid");
                return "Login: Invalid Password";
            }else{
                users.put("Login","Both");
                return "Login: Invalid Password and Username";
            }



        } catch (MongoException e) {
            e.printStackTrace();
            return "Exception";
        }
    }
}
