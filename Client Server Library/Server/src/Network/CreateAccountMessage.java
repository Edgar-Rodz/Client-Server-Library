package Network;

class CreateAccountMessage extends Message{

    String username;
     String password;
     String operationtype;

    public CreateAccountMessage(String operationtype,String username, String password){
        this.operationtype = operationtype;
        this.username = username;
        this.password = password;
        System.out.println("Create account success info");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
