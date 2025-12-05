package pl.blokaj.dbms.metastore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import pl.blokaj.dbms.filesystem.TableFilesManager;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.table.Column;
import pl.blokaj.dbms.model.table.LogicalColumnType;
import pl.blokaj.dbms.model.table.ShallowTable;
import pl.blokaj.dbms.model.table.TableSchema;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Metastore implements AutoCloseable {
    private Set<String> tablesUUID = ConcurrentHashMap.newKeySet();
    private ConcurrentHashMap<String, TableSchema> tableSchemaMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, List<String>> tableFilesMap = new ConcurrentHashMap<>();
    private transient final ConcurrentHashMap<String, TableFilesManager> tableFilesManagersMap = new ConcurrentHashMap<>();
    private static final String JSON_NAME = "store.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private transient final Path runtimeFile;

    public Metastore() throws IOException {
        Path dataDir = Path.of("data");
        Files.createDirectories(dataDir);
        runtimeFile = dataDir.resolve(JSON_NAME);

        if (!Files.exists(runtimeFile)) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(JSON_NAME)) {
                if (is != null) {
                    Files.copy(is, runtimeFile, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    // Resource not found: create empty JSON
                    mapper.writeValue(runtimeFile.toFile(), this);
                }
            }
        }

        Metastore loaded =  mapper.readValue(runtimeFile.toFile(), Metastore.class);
        this.tableSchemaMap = loaded.tableSchemaMap;
        this.tablesUUID = loaded.tablesUUID;
        this.tableFilesMap = loaded.tableFilesMap;

        for (String uuid: tablesUUID) {
            tableFilesManagersMap.put(uuid, new TableFilesManager(tableFilesMap.get(uuid), tableSchemaMap.get(uuid)));
        }
    }

    public List<ShallowTable> getShallowTables() {
        List<ShallowTable> list = new ArrayList<ShallowTable>();
        tableSchemaMap.forEach((uuid, schema) ->
                    list.add(new ShallowTable(schema.getName(), uuid))
                );
        return list;
    }

    public TableSchema getTableById(String uuid) {
        return tableSchemaMap.get(uuid);
    }

    private void deleteTableQuery(JsonNode args, List<String> fileNames, TableSchema schema) {
        Path dataDir = Path.of("data");
        for (String fileName: fileNames) {
            Path filePath = dataDir.resolve(fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Failed to delete: " + filePath + ", error: " + e.getMessage());
            }
        }
    }

    public boolean deleteTable(String uuid) {
        TableSchema table = tableSchemaMap.get(uuid);
        if (table == null) return false;
        else {
            tableFilesManagersMap.get(uuid).writeQuery(
                    (args, files, schema) -> {
                        deleteTableQuery(args, files, schema);
                        return null; // TriFunction must return something
                    },
                    null
            );

            tableFilesMap.remove(uuid);
            tableSchemaMap.remove(uuid);
            tablesUUID.remove(uuid);
            tableFilesManagersMap.remove(uuid);
            return true;
        }
    }


    public List<MultipleProblemsError.Problem> validateTablePut(JsonNode tableJson) {
        List<MultipleProblemsError.Problem> problems = new ArrayList<>();

        String tableName = tableJson.get("name").asText();
        for (TableSchema table: tableSchemaMap.values()) {
            if (table.getName().equals(tableName)) {
                problems.add(new MultipleProblemsError.Problem("Table with name '" + tableName + "' already exists", null));
            }
        }

        JsonNode columnsNode = tableJson.get("columns");

        if (columnsNode.isArray()) {
            for (int i = 0; i < columnsNode.size(); i++) {
                JsonNode columnNode = columnsNode.get(i);
                String name = columnNode.path("name").asText(null);
                String type = columnNode.path("type").asText(null);

                if (name == null || name.isEmpty()) {
                    problems.add(new MultipleProblemsError.Problem("Name of column is either null or empty", "Column index: " + i));
                }
                try {
                    LogicalColumnType.valueOf(type);
                } catch (IllegalArgumentException e) {
                    problems.add(new MultipleProblemsError.Problem("Wrong column type: " + type, "Column index: " + i));
                }
            }
        }
        else {
            problems.add(new MultipleProblemsError.Problem("Columns are null or not an array", null));
        }

        return problems;
    }

    public String createTable(TableSchema newTable) {
        String newUUID = UUID.randomUUID().toString();
        List<String> fileNames = Collections.synchronizedList(new ArrayList<>());
        tablesUUID.add(newUUID);
        tableSchemaMap.put(newUUID, newTable);
        tableFilesMap.put(newUUID, fileNames);
        tableFilesManagersMap.put(newUUID, new TableFilesManager(fileNames, newTable));
        return newUUID;
    }

    private void save() throws IOException {
        mapper.writeValue(runtimeFile.toFile(), this);
    }

    @Override
    public void close() throws IOException {
        save();
    }
}
