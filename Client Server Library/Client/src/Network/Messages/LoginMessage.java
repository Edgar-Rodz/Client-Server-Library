package Network.Messages;

public class LoginMessage extends message{

    private String username;
    private String password;
    public LoginMessage(String username, String password) {
        super("Login account");
        this.username = username;
        this.password = password;
    }

}
