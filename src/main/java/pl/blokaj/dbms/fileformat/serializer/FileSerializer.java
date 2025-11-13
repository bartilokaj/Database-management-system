package pl.blokaj.dbms.fileformat.serializer;

import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.ColumnDictionary;
import pl.blokaj.dbms.fileformat.encoding.VLQ;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileSerializer {

    public static void toFile(String fileName, Table table) throws IOException {
        String fullPath = "src/main/resources/" + fileName;
        Files.deleteIfExists(Paths.get(fullPath));
        ArrayList<Column> columns = table.getColumns();

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fullPath))) {
            VLQ.encodeSingleVLQ(columns.size(), bos);
            for (Column column : columns) {
                byte type = ColumnDictionary.getColumnType(column.getClass());
                bos.write(type);
                column.serialize(bos);
            }
        }
    }


}
