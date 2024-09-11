package Network.Messages;

public class AccountMessage extends message{

    private String username;
    public AccountMessage(String username) {

        super("Account");
        this.username = username;

    }
}
