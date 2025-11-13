package pl.blokaj.dbms.fileformat.deserializer;

import org.junit.jupiter.api.Test;
import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.app.ProcessCSV;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class OnlyStringTest {

    @Test
    public void onlyStringTest() throws IOException {
        Table originalTable = ProcessCSV.onlyString();
        String filePath = "src/main/resources/smol.dbms";

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        Table deserializedTable = FileDeserializer.deserializeFile(bis);

        byte[] original;
        byte[] deserialized;

        for (int i = 0; i < originalTable.getColumns().size(); i++) {
            ArrayList<byte[]> originalEntries = (ArrayList<byte[]>) originalTable.getColumns().get(i).getData();
            ArrayList<byte[]> deserializedEntries = (ArrayList<byte[]>) deserializedTable.getColumns().get(i).getData();
            for (int j = 0; j < originalEntries.size(); j++) {
                original = originalEntries.get(j);
                deserialized = deserializedEntries.get(j);
                assertArrayEquals(original, deserialized);
            }
        }
    }
}
