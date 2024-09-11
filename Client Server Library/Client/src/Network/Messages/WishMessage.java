package Network.Messages;

public class WishMessage extends message{

    private String bookTitle;
    private String username;

    // Constructor
    public WishMessage(String username,String bookTitle) {

        super("wish");
        this.username = username;
        this.bookTitle = bookTitle;
    }

    public String getUsername(){
        return username;
    }
}
