package Network;

public class WishMessage extends Message{
    private String username;
    private String bookTitle;

    public WishMessage(String username, String bookTitle) {
        this.username = username;
        this.bookTitle = bookTitle;
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
