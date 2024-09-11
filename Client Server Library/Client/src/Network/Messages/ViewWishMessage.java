package Network.Messages;

public class ViewWishMessage extends message{

    private String username;

    // Constructor
    public ViewWishMessage(String username) {

        super("View");
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
