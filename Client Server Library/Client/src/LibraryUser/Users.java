package LibraryUser;

public class Users {
    private static final int serialVersionID = 5;
    private String username;
    private String password;
    public Users(String username, String password){
        this.username = username;
        this.password = username;
    }

    public String getUsername(){
        return username;
    }

    public String setUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String setPassword(){
        return password;
    }

}
