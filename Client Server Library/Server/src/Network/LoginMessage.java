package Network;

public class LoginMessage extends Message{

     String username;
     String password;
    public LoginMessage(String username,String password){

        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
