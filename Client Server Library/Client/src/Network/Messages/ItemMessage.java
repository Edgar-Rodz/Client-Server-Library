package Network.Messages;

import java.io.Serializable;

public class ItemMessage implements Serializable {
    private String itemType;
    private String title;
    private String author;
    private int pages;
    private String summary;
    private String currentMember;
    private String[] previousMembers;
    private String lastCheckedOut;
    // You can add more fields as needed

    // Constructor
    public ItemMessage(String itemType, String title, String author, int pages, String summary) {
        this.itemType = itemType;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.summary = summary;
        this.currentMember = null;
        this.previousMembers = new String[5]; // assuming storing 5 previous members
        this.lastCheckedOut = null;
    }

    // Getters and setters for all fields
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(String currentMember) {
        this.currentMember = currentMember;
    }

    public String[] getPreviousMembers() {
        return previousMembers;
    }

    public void setPreviousMembers(String[] previousMembers) {
        this.previousMembers = previousMembers;
    }

    public String getLastCheckedOut() {
        return lastCheckedOut;
    }

    public void setLastCheckedOut(String lastCheckedOut) {
        this.lastCheckedOut = lastCheckedOut;
    }
}
