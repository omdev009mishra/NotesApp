import java.util.List;

/**
 * Interface defining database operations for notes
 * Demonstrates the use of Interfaces in OOP
 */
public interface DatabaseOperations<T> {
    /**
     * Save a note to the database
     * @param item The item to save
     * @throws DatabaseException if save operation fails
     */
    void save(T item) throws DatabaseException;
    
    /**
     * Update an existing note in the database
     * @param item The item to update
     * @throws DatabaseException if update operation fails
     */
    void update(T item) throws DatabaseException;
    
    /**
     * Delete a note from the database
     * @param id The ID of the item to delete
     * @throws DatabaseException if delete operation fails
     */
    void delete(int id) throws DatabaseException;
    
    /**
     * Retrieve a note by its ID
     * @param id The ID of the item to retrieve
     * @return The item with the specified ID
     * @throws DatabaseException if retrieval operation fails
     */
    T getById(int id) throws DatabaseException;
    
    /**
     * Retrieve all notes from the database
     * @return List of all items
     * @throws DatabaseException if retrieval operation fails
     */
    List<T> getAll() throws DatabaseException;
}
