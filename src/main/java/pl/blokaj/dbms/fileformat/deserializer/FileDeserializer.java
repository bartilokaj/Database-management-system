package pl.blokaj.dbms.fileformat.deserializer;

import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.ColumnDictionary;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.FileHeader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileDeserializer {


    public static Table deserializeFile(InputStream in) {
        try {
            FileHeader fileHeader = new FileHeader();
            fileHeader.setColumnNumber(VLQ.decodeSingleVLQ(in));
            Table newTable = new Table();
            for (int i = 0; i < fileHeader.getColumnNumber(); i++) {
                ColumnDeserializer<?> deserializer;
                do {
                    byte classByte = (byte) in.read();
                    System.out.println((char) classByte);
                    deserializer = DeserializerDictionary.getColumnDeserializer(classByte);
                } while (deserializer == null);

                Column next = deserializer.deserialize(in);
                newTable.addColumn(next);
            }
            return newTable;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
