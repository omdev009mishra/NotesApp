/**
 * Auto-save manager that runs in a background thread
 * Demonstrates Multithreading and Synchronization
 */
public class AutoSaveManager extends Thread {
    private volatile boolean running = true;
    private final NotesAppWithDB app;
    private final int saveIntervalMs;
    
    public AutoSaveManager(NotesAppWithDB app, int saveIntervalSeconds) {
        this.app = app;
        this.saveIntervalMs = saveIntervalSeconds * 1000;
        setDaemon(true); // Thread will terminate when main application exits
        setName("AutoSaveThread");
    }
    
    @Override
    public void run() {
        System.out.println("Auto-save thread started");
        while (running) {
            try {
                Thread.sleep(saveIntervalMs);
                if (running) {
                    System.out.println("Auto-saving...");
                    app.autoSave();
                }
            } catch (InterruptedException e) {
                System.out.println("Auto-save thread interrupted");
                break;
            }
        }
        System.out.println("Auto-save thread stopped");
    }
    
    /**
     * Stop the auto-save thread gracefully
     * Demonstrates proper thread termination
     */
    public void stopAutoSave() {
        running = false;
        interrupt();
        try {
            join(5000); // Wait up to 5 seconds for thread to finish
        } catch (InterruptedException e) {
            System.err.println("Error waiting for auto-save thread to stop: " + e.getMessage());
        }
    }
}
