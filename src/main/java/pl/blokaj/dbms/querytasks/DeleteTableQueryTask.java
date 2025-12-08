package pl.blokaj.dbms.querytasks;

import pl.blokaj.dbms.filesystem.TableFilesManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DeleteTableQueryTask implements Runnable {
    private final List<String> fileNames;
    private final TableFilesManager manager;

    public DeleteTableQueryTask(List<String> fileNames, TableFilesManager manager) {
        this.fileNames = fileNames;
        this.manager = manager;
    }

    @Override
    public void run() {
        manager.lockWriteLock();
        Path dataDir = Path.of("data");
        for (String fileName: fileNames) {
            Path filePath = dataDir.resolve(fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Failed to delete: " + filePath + ", error: " + e.getMessage());
            }
        }
        manager.unlockWriteLock();
    }
}
