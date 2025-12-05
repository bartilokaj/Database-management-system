package pl.blokaj.dbms.filesystem;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.function.IOTriFunction;
import pl.blokaj.dbms.model.table.TableSchema;
import pl.blokaj.dbms.utility.TriFunction;

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

    public <R> R readQuery(TriFunction<JsonNode, List<String>, TableSchema, R> queryFunc, JsonNode funcArgs) {
        try {
            mutex.readLock().lock();
            return queryFunc.apply(funcArgs, fileNames, tableSchema);
        }
        finally {
            mutex.readLock().unlock();
        }
    }

    public <R> R writeQuery(TriFunction<JsonNode, List<String>, TableSchema, R> queryFunc, JsonNode funcArgs) {
        try {
            mutex.writeLock().lock();
            return queryFunc.apply(funcArgs, fileNames, tableSchema);
        }
        finally {
            mutex.writeLock().unlock();
        }
    }
}
