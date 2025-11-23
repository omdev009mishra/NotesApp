import java.util.Date;

/**
 * Concrete class for text notes
 * Demonstrates Inheritance from abstract Note class
 */
public class TextNote extends Note {
    private String content;
    
    public TextNote() {
        super();
        this.content = "";
    }
    
    public TextNote(int id, String title, String content, Date createdDate, Date modifiedDate) {
        super(id, title, createdDate, modifiedDate);
        this.content = content;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public void setContent(String content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.content = content;
        this.modifiedDate = new Date();
    }
    
    @Override
    public String getType() {
        return "TEXT";
    }
}
