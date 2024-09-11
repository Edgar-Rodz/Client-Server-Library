package Network.Messages;

public abstract class message {
    // Common fields
    private String type;

    // Constructor
    protected message(String type) {
        this.type = type;
    }

    // Getter and setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
