import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Data Access Object for Note operations using JDBC
 * Demonstrates Database Connectivity (JDBC) and implementation of DatabaseOperations interface
 * Uses Collections and Generics with List<Note>
 * Implements thread-safe operations with synchronization
 */
public class NoteDAO implements DatabaseOperations<Note> {
    private static final String DB_URL = "jdbc:sqlite:notesapp.db";
    private static NoteDAO instance;
    private Connection connection;
    
    // Private constructor for singleton pattern with exception handling
    private NoteDAO() throws DatabaseException {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }
    }
    
    /**
     * Get singleton instance of NoteDAO (thread-safe)
     * Demonstrates Synchronization for thread safety
     */
    public static synchronized NoteDAO getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new NoteDAO();
        }
        return instance;
    }
    
    /**
     * Initialize database tables if they don't exist
     */
    private void initializeDatabase() throws DatabaseException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS notes (" +
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
    
    /**
     * Save a note to database
     * Demonstrates Exception Handling and JDBC operations
     */
    @Override
    public synchronized void save(Note note) throws DatabaseException {
        String sql = "INSERT INTO notes (title, content, type, image_data, created_date, modified_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, note.getTitle());
            
            if (note instanceof TextNote) {
                pstmt.setString(2, note.getContent());
                pstmt.setString(3, "TEXT");
                pstmt.setNull(4, Types.BLOB);
            } else if (note instanceof DrawingNote) {
                pstmt.setNull(2, Types.VARCHAR);
                pstmt.setString(3, "DRAWING");
                DrawingNote drawingNote = (DrawingNote) note;
                if (drawingNote.getImageData() != null) {
                    pstmt.setBytes(4, drawingNote.getImageData());
                } else {
                    pstmt.setNull(4, Types.BLOB);
                }
            }
            
            pstmt.setLong(5, note.getCreatedDate().getTime());
            pstmt.setLong(6, note.getModifiedDate().getTime());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating note failed, no rows affected.");
            }
            
            // Get the last inserted row ID using SQLite-specific function
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    note.setId(rs.getInt(1));
                } else {
                    throw new DatabaseException("Creating note failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save note", e);
        }
    }
    
    /**
     * Update an existing note
     * Demonstrates JDBC UPDATE operations with synchronization
     */
    @Override
    public synchronized void update(Note note) throws DatabaseException {
        String sql = "UPDATE notes SET title = ?, content = ?, image_data = ?, modified_date = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, note.getTitle());
            
            if (note instanceof TextNote) {
                pstmt.setString(2, note.getContent());
                pstmt.setNull(3, Types.BLOB);
            } else if (note instanceof DrawingNote) {
                pstmt.setNull(2, Types.VARCHAR);
                DrawingNote drawingNote = (DrawingNote) note;
                if (drawingNote.getImageData() != null) {
                    pstmt.setBytes(3, drawingNote.getImageData());
                } else {
                    pstmt.setNull(3, Types.BLOB);
                }
            }
            
            pstmt.setLong(4, note.getModifiedDate().getTime());
            pstmt.setInt(5, note.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating note failed, note not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update note", e);
        }
    }
    
    /**
     * Delete a note by ID
     * Demonstrates JDBC DELETE operations
     */
    @Override
    public synchronized void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM notes WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Deleting note failed, note not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete note", e);
        }
    }
    
    /**
     * Get a note by ID
     * Demonstrates JDBC SELECT operations and Polymorphism
     */
    @Override
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
    
    /**
     * Get all notes from database
     * Demonstrates Collections & Generics with List<Note>
     */
    @Override
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
    
    /**
     * Helper method to create Note object from ResultSet
     * Demonstrates Polymorphism - returns correct subclass based on type
     */
    private Note createNoteFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String type = rs.getString("type");
        Date createdDate = new Date(rs.getLong("created_date"));
        Date modifiedDate = new Date(rs.getLong("modified_date"));
        
        if ("TEXT".equals(type)) {
            String content = rs.getString("content");
            return new TextNote(id, title, content != null ? content : "", createdDate, modifiedDate);
        } else if ("DRAWING".equals(type)) {
            byte[] imageData = rs.getBytes("image_data");
            return new DrawingNote(id, title, imageData, createdDate, modifiedDate);
        } else {
            // Default to text note if type is unknown
            return new TextNote(id, title, "", createdDate, modifiedDate);
        }
    }
    
    /**
     * Close database connection
     */
    public synchronized void close() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close database connection", e);
        }
    }
}
