package Network;

public class ResetMessage extends Message{
        String username;
        String password;
        public ResetMessage(String username,String password) {

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
