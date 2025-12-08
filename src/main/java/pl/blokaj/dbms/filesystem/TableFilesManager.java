package pl.blokaj.dbms.filesystem;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.function.IOTriFunction;
import pl.blokaj.dbms.model.table.TableSchema;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class TableFilesManager {
    private final List<String> fileNames;
    private final TableSchema tableSchema;
    private final ReentrantReadWriteLock mutex = new ReentrantReadWriteLock(true);

    public TableFilesManager(List<String> fileNames, TableSchema tableSchema) {
        this.fileNames = fileNames;
        this.tableSchema = tableSchema;
    }

    public void lockReadLock() {
        mutex.readLock().lock();
    }

    public void unlockReadLock() {
        mutex.readLock().unlock();
    }

    public void lockWriteLock() {
        mutex.writeLock().lock();
    }

    public void unlockWriteLock() {
        mutex.writeLock().unlock();
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public TableSchema getTableSchema() {
        return tableSchema;
    }

    public ReentrantReadWriteLock getReadLock() {
        return mutex;
    }
}
