package Network.Messages;

public class ReturnMessage extends message{

    private String bookTitle;
    private String username;

    // Constructor
    public ReturnMessage(String username,String bookTitle) {

        super("return");
        this.username = username;
        this.bookTitle = bookTitle;
    }
}
