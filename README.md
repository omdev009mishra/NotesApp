# NotesApp - Java Swing Application with JDBC

A feature-rich notes application built with Java Swing that demonstrates core OOP principles, database connectivity, and advanced Java concepts.

## Features Implemented

### 1. **OOP Implementation**

#### Inheritance
- Abstract `Note` class serves as the base class
- `TextNote` and `DrawingNote` extend the `Note` class
- Demonstrates hierarchical class structure

#### Polymorphism
- Abstract methods in `Note` class (`getContent()`, `setContent()`, `getType()`)
- Different implementations in subclasses
- Method overriding demonstrated in concrete classes
- Runtime polymorphism with `List<Note>` collections

#### Interfaces
- `DatabaseOperations<T>` interface defines database operation contracts
- Generic interface allowing type-safe operations
- Implemented by `NoteDAO` class

#### Exception Handling
- Custom `DatabaseException` class for database-specific errors
- Try-catch blocks throughout the application
- Proper exception propagation and handling
- SQLException wrapped in custom exceptions

### 2. **Collections & Generics**

- `List<Note>` for storing and managing notes
- `ArrayList<Note>` as concrete implementation
- Generic `DatabaseOperations<T>` interface
- `DefaultListModel<Note>` for GUI list management
- Type-safe collections throughout the application

### 3. **Multithreading & Synchronization**

#### Multithreading
- `AutoSaveManager` class extends `Thread`
- Background thread for automatic note saving
- Daemon thread that terminates with main application
- Proper thread lifecycle management

#### Synchronization
- `synchronized` keyword on database operation methods
- Thread-safe singleton pattern in `NoteDAO.getInstance()`
- `volatile` keyword for thread visibility
- `SwingUtilities.invokeLater()` for GUI thread safety

### 4. **Database Operations Classes**

#### NoteDAO (Data Access Object)
- Singleton pattern for single database connection
- CRUD operations (Create, Read, Update, Delete)
- Thread-safe operations
- Implements `DatabaseOperations<Note>` interface

#### Note Classes
- `Note` - Abstract base class
- `TextNote` - For text-based notes
- `DrawingNote` - For drawing/image notes

### 5. **JDBC Database Connectivity**

#### Database Setup
- SQLite database for lightweight, file-based storage
- Automatic table creation on first run
- Connection pooling through singleton pattern

#### JDBC Operations
- **INSERT**: Save new notes to database
- **UPDATE**: Modify existing notes
- **DELETE**: Remove notes from database
- **SELECT**: Query single or multiple notes
- Prepared statements to prevent SQL injection
- Proper resource management with try-with-resources

#### Database Schema
```sql
CREATE TABLE notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT,
    type TEXT NOT NULL,
    image_data BLOB,
    created_date INTEGER NOT NULL,
    modified_date INTEGER NOT NULL
)
```

## Project Structure

```
NotesApp/
├── src/
│   ├── NotesApp.java              # Original simple notes app
│   ├── NotesAppWithDB.java        # Enhanced app with database
│   ├── Note.java                  # Abstract base class
│   ├── TextNote.java              # Text note implementation
│   ├── DrawingNote.java           # Drawing note implementation
│   ├── DatabaseOperations.java   # Generic interface
│   ├── NoteDAO.java               # Database access object
│   ├── DatabaseException.java    # Custom exception
│   ├── AutoSaveManager.java       # Multithreading for auto-save
│   └── TestNotesApp.java          # Test program
├── lib/
│   ├── sqlite-jdbc-3.44.1.0.jar   # SQLite JDBC driver
│   ├── slf4j-api-2.0.9.jar        # SLF4J API
│   └── slf4j-simple-2.0.9.jar     # SLF4J implementation
└── notesapp.db                    # SQLite database (created at runtime)
```

## How to Build and Run

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- All JAR files in the `lib` directory

### Compile
```bash
cd src
javac -cp "../lib/*:." *.java
```

### Run the GUI Application
```bash
java -cp "../lib/*:." NotesAppWithDB
```

### Run Tests
```bash
java -cp "../lib/*:." TestNotesApp
```

## Key Design Patterns

1. **Singleton Pattern**: `NoteDAO.getInstance()` ensures single database connection
2. **Data Access Object (DAO)**: Separates database logic from business logic
3. **Template Method**: Abstract `Note` class defines structure for subclasses
4. **Factory Method**: `createNoteFromResultSet()` creates appropriate note types

## OOP Principles Demonstrated

- **Encapsulation**: Private fields with public getters/setters
- **Abstraction**: Abstract `Note` class and `DatabaseOperations` interface
- **Inheritance**: Class hierarchy with `Note` as base class
- **Polymorphism**: Runtime type determination and method overriding

## Testing

Run `TestNotesApp.java` to verify all features:
- Database initialization
- Note creation (inheritance & polymorphism)
- CRUD operations (JDBC)
- Collections & generics
- Multithreading
- Exception handling
- Interface implementation

## Dependencies

- **SQLite JDBC Driver** (v3.44.1.0): Database connectivity
- **SLF4J** (v2.0.9): Logging framework required by SQLite JDBC

## License

This project is for educational purposes demonstrating Java OOP concepts and JDBC.
