package GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private final StringProperty title;
    private final StringProperty author;
    // Add more properties as needed

    // Constructor
    public Book(String title, String author, String genre, String isbn, int pages) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        // Initialize more properties as needed
    }

    // Getter methods for properties
    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }
    // You can add more methods as needed, such as toString(), equals(), hashCode(), etc.
}
