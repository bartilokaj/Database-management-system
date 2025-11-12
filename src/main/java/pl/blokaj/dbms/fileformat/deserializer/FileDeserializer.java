package pl.blokaj.dbms.fileformat.deserializer;

import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.ColumnDictionary;
import pl.blokaj.dbms.fileformat.encoding.VLQ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileDeserializer {


    public static Table deserializeFile(InputStream in) {
        try {
            long filesize = VLQ.decodeSingleVLQ(in);
            long columnNumber = VLQ.decodeSingleVLQ(in);
            Table newTable = new Table();
            for (int i = 0; i < columnNumber; i++) {
                byte classByte = (byte) in.read();
                ColumnDeserializer<?> deserializer = DeserializerDictionary.getColumnDeserializer(classByte);
                Column next = deserializer.deserialize(in);
                newTable.addColumn(next);
            }
            return newTable;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
