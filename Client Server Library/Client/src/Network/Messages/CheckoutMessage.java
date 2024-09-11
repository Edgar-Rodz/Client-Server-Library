package Network.Messages;

public class CheckoutMessage extends message {
    private String bookTitle;
    private String username;

    // Constructor
    public CheckoutMessage(String username,String bookTitle) {

        super("checkout");
        this.username = username;
        this.bookTitle = bookTitle;
    }

    // Getter and setter for bookTitle
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getUsername(){
        return username;
    }
}
