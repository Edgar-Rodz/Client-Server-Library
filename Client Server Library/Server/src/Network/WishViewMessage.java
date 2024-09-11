package Network;

public class WishViewMessage extends Message{
    private String username;


    public WishViewMessage(String username, String bookTitle) {
        this.username = username;

    }

    public String getUsername() {
        return username;
    }


}
