package pl.blokaj.dbms.metastore;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.blokaj.dbms.model.table.ShallowTable;
import pl.blokaj.dbms.model.table.TableSchema;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Metastore implements AutoCloseable {
    private final ArrayList<String> tablesUUID;
    private final HashMap<String, TableSchema> tableSchemaMap;
    private static final String JSON_NAME = "store.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private final Path runtimeFile;

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

    private void save() throws IOException {
        mapper.writeValue(runtimeFile.toFile(), this);
    }

    @Override
    public void close() throws IOException {
        save();
    }
}
