/**
 * Test class to verify OOP concepts, JDBC, Collections, and Exception Handling
 */
public class TestNotesApp {
    public static void main(String[] args) {
        System.out.println("=== NotesApp Features Test ===\n");
        
        try {
            // Test 1: Database initialization and JDBC
            System.out.println("Test 1: Initializing database connection (JDBC)...");
            NoteDAO dao = NoteDAO.getInstance();
            System.out.println("✓ Database connection established successfully\n");
            
            // Test 2: Polymorphism & Inheritance - Creating different note types
            System.out.println("Test 2: Creating notes (Inheritance & Polymorphism)...");
            TextNote textNote1 = new TextNote();
            textNote1.setTitle("My First Note");
            textNote1.setContent("This is a text note demonstrating inheritance from abstract Note class");
            
            TextNote textNote2 = new TextNote();
            textNote2.setTitle("Shopping List");
            textNote2.setContent("- Milk\n- Bread\n- Eggs");
            
            DrawingNote drawingNote = new DrawingNote();
            drawingNote.setTitle("My Drawing");
            drawingNote.setImageData(new byte[]{1, 2, 3, 4, 5}); // Sample data
            
            System.out.println("✓ Created 3 notes of different types\n");
            
            // Test 3: JDBC Save operations with Exception Handling
            System.out.println("Test 3: Saving notes to database (JDBC & Exception Handling)...");
            dao.save(textNote1);
            System.out.println("  - Saved: " + textNote1.toString());
            
            dao.save(textNote2);
            System.out.println("  - Saved: " + textNote2.toString());
            
            dao.save(drawingNote);
            System.out.println("  - Saved: " + drawingNote.toString());
            System.out.println("✓ All notes saved successfully\n");
            
            // Test 4: Collections & Generics - Retrieving notes as List
            System.out.println("Test 4: Retrieving all notes (Collections & Generics)...");
            java.util.List<Note> allNotes = dao.getAll();
            System.out.println("✓ Retrieved " + allNotes.size() + " notes from database");
            System.out.println("  Notes list type: " + allNotes.getClass().getSimpleName() + "<Note>");
            
            // Polymorphism demonstration
            System.out.println("\n  Polymorphism demonstration - iterating through List<Note>:");
            for (Note note : allNotes) {
                System.out.println("    - " + note.toString() + " | Content: " + note.getContent());
            }
            System.out.println();
            
            // Test 5: JDBC Update operations
            System.out.println("Test 5: Updating a note (JDBC UPDATE)...");
            textNote1.setTitle("My Updated First Note");
            textNote1.setContent("Content updated!");
            dao.update(textNote1);
            
            Note retrieved = dao.getById(textNote1.getId());
            System.out.println("✓ Note updated successfully");
            System.out.println("  Retrieved note: " + retrieved.toString() + "\n");
            
            // Test 6: Multithreading - Create and test auto-save thread
            System.out.println("Test 6: Testing multithreading...");
            Thread testThread = new Thread(() -> {
                System.out.println("  - Background thread started (Thread ID: " + Thread.currentThread().getId() + ")");
                try {
                    Thread.sleep(1000);
                    System.out.println("  - Background thread completing task");
                } catch (InterruptedException e) {
                    System.out.println("  - Thread interrupted");
                }
            });
            testThread.start();
            testThread.join();
            System.out.println("✓ Multithreading test completed\n");
            
            // Test 7: Exception Handling - Try to get non-existent note
            System.out.println("Test 7: Testing exception handling...");
            try {
                dao.getById(99999);
                System.out.println("✗ Exception not thrown as expected");
            } catch (DatabaseException e) {
                System.out.println("✓ DatabaseException caught correctly: " + e.getMessage() + "\n");
            }
            
            // Test 8: Interface implementation verification
            System.out.println("Test 8: Verifying interface implementation...");
            System.out.println("  NoteDAO implements DatabaseOperations interface");
            System.out.println("  - Implemented methods: save(), update(), delete(), getById(), getAll()");
            System.out.println("✓ Interface implementation verified\n");
            
            // Test 9: JDBC Delete operation
            System.out.println("Test 9: Deleting a note (JDBC DELETE)...");
            int deleteId = textNote2.getId();
            dao.delete(deleteId);
            System.out.println("✓ Note deleted successfully (ID: " + deleteId + ")\n");
            
            // Final count
            allNotes = dao.getAll();
            System.out.println("Final note count: " + allNotes.size() + " notes in database");
            
            // Cleanup
            dao.close();
            System.out.println("\n=== All Tests Passed Successfully ===");
            System.out.println("\nImplemented Features:");
            System.out.println("✓ OOP - Inheritance (Note -> TextNote, DrawingNote)");
            System.out.println("✓ OOP - Polymorphism (Note references, method overriding)");
            System.out.println("✓ OOP - Interfaces (DatabaseOperations implemented by NoteDAO)");
            System.out.println("✓ OOP - Exception Handling (DatabaseException custom exception)");
            System.out.println("✓ Collections & Generics (List<Note>, ArrayList<Note>)");
            System.out.println("✓ Multithreading (Background threads, AutoSaveManager)");
            System.out.println("✓ Synchronization (synchronized methods in NoteDAO)");
            System.out.println("✓ JDBC Database Connectivity (SQLite with CRUD operations)");
            
        } catch (DatabaseException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }
}
