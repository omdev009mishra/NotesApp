import java.util.Date;

/**
 * Concrete class for drawing notes
 * Demonstrates Inheritance from abstract Note class
 */
public class DrawingNote extends Note {
    private byte[] imageData;
    
    public DrawingNote() {
        super();
    }
    
    public DrawingNote(int id, String title, byte[] imageData, Date createdDate, Date modifiedDate) {
        super(id, title, createdDate, modifiedDate);
        this.imageData = imageData;
    }
    
    @Override
    public String getContent() {
        return imageData != null ? "Drawing with " + imageData.length + " bytes" : "Empty drawing";
    }
    
    @Override
    public void setContent(String content) {
        // For drawing notes, content is set via setImageData
        this.modifiedDate = new Date();
    }
    
    public byte[] getImageData() {
        return imageData;
    }
    
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
        this.modifiedDate = new Date();
    }
    
    @Override
    public String getType() {
        return "DRAWING";
    }
}
