package pl.blokaj.dbms.fileformat.deserializer;

import com.github.luben.zstd.ZstdInputStream;
import pl.blokaj.dbms.tablepage.TablePage;
import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.FileHeader;

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
    public static TablePage deserializeFile(InputStream in) {
        try {
            ZstdInputStream zis = new ZstdInputStream(in);
            FileHeader fileHeader = new FileHeader();
            fileHeader.setColumnNumber(VLQ.decodeSingleVLQ(zis));
            TablePage newTablePage = new TablePage();
            for (int i = 0; i < fileHeader.getColumnNumber(); i++) {
                ColumnPageDeserializer<?> deserializer;
                byte classByte = (byte) zis.read();
                deserializer = DeserializerDictionary.getColumnDeserializer(classByte);
                ColumnPage next = deserializer.deserialize(zis);
                newTablePage.addColumn(next);
            }
            return newTablePage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
