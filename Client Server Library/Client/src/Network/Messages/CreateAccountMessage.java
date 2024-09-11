package Network.Messages;

public class CreateAccountMessage extends message {
    private String username;
    private String password;


    // Constructor
    public CreateAccountMessage(String username, String password) {
        super("create account");
        this.username = username;
        this.password = password;
    }

    // Getters and setters for username and password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
