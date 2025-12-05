package pl.blokaj.dbms.fileformat.deserializer;

import org.junit.jupiter.api.Test;
import pl.blokaj.dbms.tablepage.TablePage;
import pl.blokaj.dbms.app.GenerateCSV;
import pl.blokaj.dbms.app.ProcessCSV;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class IntAndStringTest {

    @Test
    public void IntAndStringTest() throws IOException {
        String filePath = "src/main/resources/intAndString.dbms";
        GenerateCSV.generateOnlyInt(8192);
        TablePage originalTablePage = ProcessCSV.intAndString();

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        TablePage deserializedTablePage = FileDeserializer.deserializeFile(bis);

        long[] originalLong = (long[]) originalTablePage.getColumns().get(0).getData();
        long[] deserializedLong = (long[]) deserializedTablePage.getColumns().get(0).getData();
        assertArrayEquals(originalLong, deserializedLong);

        byte[] original;
        byte[] deserialized;

        ArrayList<byte[]> originalEntries = (ArrayList<byte[]>) originalTablePage.getColumns().get(1).getData();
        ArrayList<byte[]> deserializedEntries = (ArrayList<byte[]>) deserializedTablePage.getColumns().get(1).getData();
        for (int j = 0; j < originalEntries.size(); j++) {
            original = originalEntries.get(j);
            deserialized = deserializedEntries.get(j);
            assertArrayEquals(original, deserialized);
        }
    }
}
