# Code Examples - Features Demonstration

This document provides concrete code examples demonstrating each implemented feature.

## 1. OOP - Inheritance

### Abstract Base Class
```java
// Note.java - Abstract base class
public abstract class Note {
    protected int id;
    protected String title;
    protected Date createdDate;
    protected Date modifiedDate;
    
    // Abstract methods - must be implemented by subclasses
    public abstract String getContent();
    public abstract void setContent(String content);
    public abstract String getType();
}
```

### Concrete Subclasses
```java
// TextNote.java - Inherits from Note
public class TextNote extends Note {
    private String content;
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public String getType() {
        return "TEXT";
    }
}

// DrawingNote.java - Also inherits from Note
public class DrawingNote extends Note {
    private byte[] imageData;
    
    @Override
    public String getContent() {
        return "Drawing with " + imageData.length + " bytes";
    }
    
    @Override
    public String getType() {
        return "DRAWING";
    }
}
```

## 2. OOP - Polymorphism

### Runtime Polymorphism Example
```java
// Base class reference pointing to different subclass objects
Note textNote = new TextNote();
Note drawingNote = new DrawingNote();

// Polymorphic method calls - calls appropriate subclass method
System.out.println(textNote.getType());    // Output: "TEXT"
System.out.println(drawingNote.getType()); // Output: "DRAWING"

// Polymorphic collections
List<Note> notes = new ArrayList<>();
notes.add(new TextNote());
notes.add(new DrawingNote());

// Iterate using polymorphism
for (Note note : notes) {
    System.out.println(note.getType());     // Calls appropriate method
    System.out.println(note.getContent());  // Runtime binding
}
```

### Method Overriding
```java
// NoteDAO.java - Polymorphic retrieval
private Note createNoteFromResultSet(ResultSet rs) throws SQLException {
    String type = rs.getString("type");
    
    if ("TEXT".equals(type)) {
        return new TextNote(...);  // Returns TextNote instance
    } else if ("DRAWING".equals(type)) {
        return new DrawingNote(...); // Returns DrawingNote instance
    }
    
    // All returned as Note type - polymorphism in action
}
```

## 3. OOP - Interfaces

### Interface Definition
```java
// DatabaseOperations.java - Generic interface
public interface DatabaseOperations<T> {
    void save(T item) throws DatabaseException;
    void update(T item) throws DatabaseException;
    void delete(int id) throws DatabaseException;
    T getById(int id) throws DatabaseException;
    List<T> getAll() throws DatabaseException;
}
```

### Interface Implementation
```java
// NoteDAO.java - Implements the interface
public class NoteDAO implements DatabaseOperations<Note> {
    
    @Override
    public synchronized void save(Note note) throws DatabaseException {
        String sql = "INSERT INTO notes (title, content, ...) VALUES (?, ?, ...)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Implementation
        }
    }
    
    @Override
    public synchronized List<Note> getAll() throws DatabaseException {
        List<Note> notes = new ArrayList<>();
        // Implementation
        return notes;
    }
    
    // ... other interface methods
}
```

### Using Interface Type
```java
// Programming to interface, not implementation
DatabaseOperations<Note> dao = NoteDAO.getInstance();
dao.save(note);
List<Note> allNotes = dao.getAll();
```

## 4. OOP - Exception Handling

### Custom Exception Class
```java
// DatabaseException.java
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### Exception Throwing
```java
// NoteDAO.java
public synchronized void delete(int id) throws DatabaseException {
    String sql = "DELETE FROM notes WHERE id = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows == 0) {
            throw new DatabaseException("Note not found");
        }
    } catch (SQLException e) {
        throw new DatabaseException("Failed to delete note", e);
    }
}
```

### Exception Handling
```java
// NotesAppWithDB.java
private void deleteSelectedNote() {
    try {
        noteDAO.delete(selectedNote.getId());
        System.out.println("Note deleted successfully");
    } catch (DatabaseException e) {
        JOptionPane.showMessageDialog(this, 
            "Failed to delete note: " + e.getMessage(), 
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
```

## 5. Collections & Generics

### Generic Interface
```java
// Using type parameter T
public interface DatabaseOperations<T> {
    List<T> getAll() throws DatabaseException;
}
```

### Type-Safe Collections
```java
// NotesAppWithDB.java
private List<Note> allNotes;           // Generic List
private DefaultListModel<Note> notesListModel;  // Generic ListModel

// Initialize
allNotes = new ArrayList<>();          // Type-safe ArrayList

// Add items
TextNote note = new TextNote();
allNotes.add(note);                    // Type-safe add

// Retrieve items
for (Note note : allNotes) {          // No casting needed
    System.out.println(note.getTitle());
}
```

### Generic DAO Implementation
```java
// NoteDAO.java
public synchronized List<Note> getAll() throws DatabaseException {
    List<Note> notes = new ArrayList<>();  // Generic collection
    
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM notes")) {
        
        while (rs.next()) {
            notes.add(createNoteFromResultSet(rs));  // Type-safe add
        }
    }
    
    return notes;  // Returns List<Note>
}
```

## 6. Multithreading

### Thread Class
```java
// AutoSaveManager.java - Extends Thread
public class AutoSaveManager extends Thread {
    private volatile boolean running = true;
    private final NotesAppWithDB app;
    
    public AutoSaveManager(NotesAppWithDB app, int saveIntervalSeconds) {
        this.app = app;
        setDaemon(true);  // Daemon thread
        setName("AutoSaveThread");
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(30000);  // Sleep 30 seconds
                if (running) {
                    app.autoSave();   // Call auto-save
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
```

### Starting and Stopping Threads
```java
// NotesAppWithDB.java
public NotesAppWithDB() {
    // Start auto-save thread
    autoSaveManager = new AutoSaveManager(this, 30);
    autoSaveManager.start();
}

private void closeApplication() {
    // Stop auto-save thread
    if (autoSaveManager != null) {
        autoSaveManager.stopAutoSave();
    }
}
```

### Thread-Safe GUI Updates
```java
// AutoSaveManager calls this
public synchronized void autoSave() {
    // Use SwingUtilities for thread-safe GUI updates
    SwingUtilities.invokeLater(() -> {
        if (currentNote != null) {
            saveCurrentNote();
        }
    });
}
```

## 7. Synchronization

### Synchronized Methods
```java
// NoteDAO.java - All database methods are synchronized
public class NoteDAO implements DatabaseOperations<Note> {
    
    public synchronized void save(Note note) throws DatabaseException {
        // Thread-safe save operation
    }
    
    public synchronized void update(Note note) throws DatabaseException {
        // Thread-safe update operation
    }
    
    public synchronized List<Note> getAll() throws DatabaseException {
        // Thread-safe retrieval
    }
}
```

### Synchronized Singleton
```java
// NoteDAO.java - Thread-safe singleton pattern
private static NoteDAO instance;

public static synchronized NoteDAO getInstance() throws DatabaseException {
    if (instance == null) {
        instance = new NoteDAO();
    }
    return instance;
}
```

### Volatile Keyword
```java
// AutoSaveManager.java - For visibility across threads
private volatile boolean running = true;

public void stopAutoSave() {
    running = false;  // Visible to run() method in another thread
    interrupt();
}
```

## 8. JDBC - Database Connection

### Driver Loading and Connection
```java
// NoteDAO.java
private static final String DB_URL = "jdbc:sqlite:notesapp.db";
private Connection connection;

private NoteDAO() throws DatabaseException {
    try {
        // Load JDBC driver
        Class.forName("org.sqlite.JDBC");
        
        // Establish connection
        connection = DriverManager.getConnection(DB_URL);
        
        // Initialize database
        initializeDatabase();
    } catch (ClassNotFoundException e) {
        throw new DatabaseException("SQLite JDBC driver not found", e);
    } catch (SQLException e) {
        throw new DatabaseException("Failed to connect to database", e);
    }
}
```

### Table Creation
```java
// NoteDAO.java
private void initializeDatabase() throws DatabaseException {
    String createTableSQL = 
        "CREATE TABLE IF NOT EXISTS notes (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "title TEXT NOT NULL, " +
        "content TEXT, " +
        "type TEXT NOT NULL, " +
        "image_data BLOB, " +
        "created_date INTEGER NOT NULL, " +
        "modified_date INTEGER NOT NULL)";
    
    try (Statement stmt = connection.createStatement()) {
        stmt.execute(createTableSQL);
    } catch (SQLException e) {
        throw new DatabaseException("Failed to initialize database", e);
    }
}
```

## 9. JDBC - CRUD Operations

### INSERT (Create)
```java
public synchronized void save(Note note) throws DatabaseException {
    String sql = "INSERT INTO notes (title, content, type, image_data, " +
                 "created_date, modified_date) VALUES (?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, note.getTitle());
        pstmt.setString(2, note.getContent());
        pstmt.setString(3, note.getType());
        pstmt.setBytes(4, imageData);
        pstmt.setLong(5, note.getCreatedDate().getTime());
        pstmt.setLong(6, note.getModifiedDate().getTime());
        
        pstmt.executeUpdate();
        
        // Get generated ID
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
            if (rs.next()) {
                note.setId(rs.getInt(1));
            }
        }
    } catch (SQLException e) {
        throw new DatabaseException("Failed to save note", e);
    }
}
```

### SELECT (Read)
```java
public synchronized Note getById(int id) throws DatabaseException {
    String sql = "SELECT * FROM notes WHERE id = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return createNoteFromResultSet(rs);
            } else {
                throw new DatabaseException("Note with ID " + id + " not found");
            }
        }
    } catch (SQLException e) {
        throw new DatabaseException("Failed to retrieve note", e);
    }
}

public synchronized List<Note> getAll() throws DatabaseException {
    List<Note> notes = new ArrayList<>();
    String sql = "SELECT * FROM notes ORDER BY modified_date DESC";
    
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            notes.add(createNoteFromResultSet(rs));
        }
    } catch (SQLException e) {
        throw new DatabaseException("Failed to retrieve notes", e);
    }
    
    return notes;
}
```

### UPDATE
```java
public synchronized void update(Note note) throws DatabaseException {
    String sql = "UPDATE notes SET title = ?, content = ?, " +
                 "image_data = ?, modified_date = ? WHERE id = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, note.getTitle());
        pstmt.setString(2, note.getContent());
        pstmt.setBytes(3, imageData);
        pstmt.setLong(4, note.getModifiedDate().getTime());
        pstmt.setInt(5, note.getId());
        
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows == 0) {
            throw new DatabaseException("Note not found");
        }
    } catch (SQLException e) {
        throw new DatabaseException("Failed to update note", e);
    }
}
```

### DELETE
```java
public synchronized void delete(int id) throws DatabaseException {
    String sql = "DELETE FROM notes WHERE id = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows == 0) {
            throw new DatabaseException("Note not found");
        }
    } catch (SQLException e) {
        throw new DatabaseException("Failed to delete note", e);
    }
}
```

## 10. Complete Usage Example

```java
// TestNotesApp.java - Demonstrating all features together
public class TestNotesApp {
    public static void main(String[] args) {
        try {
            // 1. JDBC Connection
            NoteDAO dao = NoteDAO.getInstance();
            
            // 2. Inheritance & Polymorphism - Create different note types
            Note textNote = new TextNote();
            textNote.setTitle("My Note");
            textNote.setContent("Content");
            
            Note drawingNote = new DrawingNote();
            drawingNote.setTitle("My Drawing");
            
            // 3. JDBC INSERT with Exception Handling
            dao.save(textNote);
            dao.save(drawingNote);
            
            // 4. Collections & Generics - Retrieve as List<Note>
            List<Note> allNotes = dao.getAll();
            
            // 5. Polymorphism - Iterate through polymorphic collection
            for (Note note : allNotes) {
                System.out.println(note.getType() + ": " + note.getTitle());
            }
            
            // 6. JDBC UPDATE
            textNote.setTitle("Updated Title");
            dao.update(textNote);
            
            // 7. JDBC SELECT by ID
            Note retrieved = dao.getById(textNote.getId());
            
            // 8. JDBC DELETE
            dao.delete(drawingNote.getId());
            
            // 9. Cleanup
            dao.close();
            
        } catch (DatabaseException e) {
            // Exception Handling
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## Summary

All features demonstrated with working code examples:
- ✅ Inheritance (Note → TextNote, DrawingNote)
- ✅ Polymorphism (abstract methods, runtime binding)
- ✅ Interfaces (DatabaseOperations<T>)
- ✅ Exception Handling (DatabaseException)
- ✅ Collections (List<Note>, ArrayList<Note>)
- ✅ Generics (Type parameters, type-safe collections)
- ✅ Multithreading (AutoSaveManager extends Thread)
- ✅ Synchronization (synchronized methods, volatile)
- ✅ JDBC (Connection, CRUD operations, PreparedStatement)
