package Network;

public class ReturnMessage extends Message{
    private String username;
    private String bookTitle;

    public ReturnMessage(String username, String bookTitle) {
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
