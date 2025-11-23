import java.util.Date;

/**
 * Abstract base class for all note types
 * Demonstrates Inheritance and Polymorphism in OOP
 */
public abstract class Note {
    protected int id;
    protected String title;
    protected Date createdDate;
    protected Date modifiedDate;
    
    public Note() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
    }
    
    public Note(int id, String title, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        this.modifiedDate = new Date();
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    /**
     * Abstract method to get note content
     * Demonstrates Polymorphism - each subclass will implement this differently
     */
    public abstract String getContent();
    
    /**
     * Abstract method to set note content
     */
    public abstract void setContent(String content);
    
    /**
     * Abstract method to get note type
     */
    public abstract String getType();
    
    @Override
    public String toString() {
        return getType() + ": " + title + " (ID: " + id + ")";
    }
}
