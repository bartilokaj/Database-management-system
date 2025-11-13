package pl.blokaj.dbms.fileformat.deserializer;

import com.github.luben.zstd.ZstdInputStream;
import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.ColumnDictionary;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.FileHeader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handles file deserializing
 */
public class FileDeserializer {

    /**
     * Processes the file, adds Zstd layer to input for decompressing, uses dictionary to get correct deserializer to the data
     * @param in File input stream
     * @return Deserialized Table
     */
    public static Table deserializeFile(InputStream in) {
        try {
            ZstdInputStream zis = new ZstdInputStream(in);
            FileHeader fileHeader = new FileHeader();
            fileHeader.setColumnNumber(VLQ.decodeSingleVLQ(zis));
            Table newTable = new Table();
            for (int i = 0; i < fileHeader.getColumnNumber(); i++) {
                ColumnDeserializer<?> deserializer;
                byte classByte = (byte) zis.read();
                deserializer = DeserializerDictionary.getColumnDeserializer(classByte);
                Column next = deserializer.deserialize(zis);
                newTable.addColumn(next);
            }
            return newTable;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
