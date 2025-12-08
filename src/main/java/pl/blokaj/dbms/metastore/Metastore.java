package pl.blokaj.dbms.metastore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pl.blokaj.dbms.filesystem.TableFilesManager;
import pl.blokaj.dbms.model.error.Error;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.table.LogicalColumnType;
import pl.blokaj.dbms.model.table.ShallowTable;
import pl.blokaj.dbms.model.table.TableSchema;
import pl.blokaj.dbms.querytasks.DeleteTableQueryTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Metastore implements AutoCloseable {
    private Set<String> tableUuids = ConcurrentHashMap.newKeySet();
    private ConcurrentHashMap<String, TableSchema> tableSchemaMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, List<String>> tableFilesMap = new ConcurrentHashMap<>();
    private transient ConcurrentHashMap<String, TableFilesManager> tableFilesManagersMap = new ConcurrentHashMap<>();
    private transient ConcurrentHashMap<String, String> nameToUuidMap;
    private static final String JSON_NAME = "store.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private transient Path runtimeFile;

    // Simple constructor, initializes transient fields

    /**
     * Constructor does not write to file.
     * Use load() to create or read from JSON.
     */
    public Metastore() {
        tableFilesManagersMap = new ConcurrentHashMap<>();
        nameToUuidMap = new ConcurrentHashMap<>();
    }

    /**
     * Static factory method to safely load the metastore.
     */
    public static Metastore load() throws IOException {
        Path dataDir = Path.of("data");
        Files.createDirectories(dataDir);
        Path runtimeFile = dataDir.resolve(JSON_NAME);

        // If file doesn't exist, try copying resource or create empty JSON
        if (!Files.exists(runtimeFile)) {
            try (InputStream is = Metastore.class.getClassLoader().getResourceAsStream(JSON_NAME)) {
                if (is != null) {
                    Files.copy(is, runtimeFile, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Metastore empty = new Metastore();
                    mapper.writeValue(runtimeFile.toFile(), empty);
                }
            }
        }

        // Read the JSON
        Metastore loaded = mapper.readValue(runtimeFile.toFile(), Metastore.class);

        // Initialize transient/runtime-only maps
        loaded.tableFilesManagersMap = new ConcurrentHashMap<>();
        loaded.nameToUuidMap = new ConcurrentHashMap<>();
        for (String uuid : loaded.tableUuids) {
            loaded.tableFilesManagersMap.put(
                    uuid,
                    new TableFilesManager(
                            loaded.tableFilesMap.get(uuid),
                            loaded.tableSchemaMap.get(uuid))
            );
            loaded.nameToUuidMap.put(
                    loaded.tableSchemaMap.get(uuid).getName(),
                    uuid
            );
        }

        loaded.runtimeFile = runtimeFile;

        return loaded;
    }

    @JsonIgnore
    public List<ShallowTable> getShallowTables() {
        List<ShallowTable> list = new ArrayList<ShallowTable>();
        tableSchemaMap.forEach((uuid, schema) ->
                    list.add(new ShallowTable(schema.getName(), uuid))
                );
        return list;
    }
    @JsonIgnore
    public TableSchema getTableById(String uuid) {
        return tableSchemaMap.get(uuid);
    }



    public boolean deleteTable(String uuid) {
        TableSchema table = tableSchemaMap.get(uuid);
        if (table == null) return false;
        else {
            TableFilesManager filesManager = tableFilesManagersMap.get(uuid);
            List<String> fileNames = tableFilesMap.get(uuid);
            Thread deleteTableThread = new Thread(new DeleteTableQueryTask(fileNames, filesManager));
            deleteTableThread.start();

            tableSchemaMap.remove(uuid);
            tableUuids.remove(uuid);
            tableFilesMap.remove(uuid);
            tableFilesManagersMap.remove(uuid);
            nameToUuidMap.remove(table.getName());
            return true;
        }
    }


    public List<MultipleProblemsError.Problem> validateTablePut(JsonNode tableJson) {
        List<MultipleProblemsError.Problem> problems = new ArrayList<>();

        JsonNode nameNode =  tableJson.get("name");
        if (nameNode != null && !nameNode.isNull()) {
            String tableName = tableJson.get("name").asText();
            for (TableSchema table: tableSchemaMap.values()) {
                if (table.getName().equals(tableName)) {
                    problems.add(new MultipleProblemsError.Problem("Table with this name already exists", tableName));
                }
            }
        } else {
            problems.add(new MultipleProblemsError.Problem("Specify table name", null));
        }

        JsonNode columnsNode = tableJson.get("columns");

        if (columnsNode != null && !columnsNode.isNull() && columnsNode.isArray()) {
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
        String newUuid = UUID.randomUUID().toString();
        List<String> fileNames = Collections.synchronizedList(new ArrayList<>());
        tableUuids.add(newUuid);
        tableSchemaMap.put(newUuid, newTable);
        tableFilesMap.put(newUuid, fileNames);
        tableFilesManagersMap.put(newUuid, new TableFilesManager(fileNames, newTable));
        nameToUuidMap.put(newTable.getName(), newUuid);
        return newUuid;
    }
    @JsonIgnore
    public String getTableUuid(String tableName) {
        return nameToUuidMap.get(tableName);
    }
    @JsonIgnore
    public Set<String> getTableColumnNames(String tableName) {
        Set<String> tableColumnNames = new HashSet<>();
        String tableUuid = getTableUuid(tableName);
        tableSchemaMap.get(tableUuid).getColumns().forEach(column -> {
            tableColumnNames.add(column.getName());
        });
        return tableColumnNames;
    }
    @JsonIgnore
    public TableFilesManager getTableFileManager(String uuid) {
        return tableFilesManagersMap.get(uuid);
    }

    @Override
    public void close() throws IOException {
        System.out.println("Im in closing!");
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Files.deleteIfExists(runtimeFile);
        mapper.writeValue(runtimeFile.toFile(), this);
    }

    public Set<String> getTableUuids() {
        return tableUuids;
    }

    public ConcurrentHashMap<String, TableSchema> getTableSchemaMap() {
        return tableSchemaMap;
    }

    public ConcurrentHashMap<String, List<String>> getTableFilesMap() {
        return tableFilesMap;
    }
}
