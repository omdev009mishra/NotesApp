/**
 * Custom exception class for database operations
 * Demonstrates Exception Handling in OOP
 */
public class DatabaseException extends Exception {
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
