package Network.Messages;

public class ResetMessage extends message{
    private String username;
    private String password;
    public ResetMessage(String username, String password) {
        super("Reset");
        this.username = username;
        this.password = password;
    }
}
