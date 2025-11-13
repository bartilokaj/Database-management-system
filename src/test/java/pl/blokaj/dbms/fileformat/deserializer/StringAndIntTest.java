package pl.blokaj.dbms.fileformat.deserializer;

import org.junit.jupiter.api.Test;
import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.app.ProcessCSV;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class StringAndIntTest {

    @Test
    public void StringAndIntTest() throws IOException {
        Table originalTable = ProcessCSV.stringAndInt();
        String filePath = "src/main/resources/stringAndInt.dbms";

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        Table deserializedTable = FileDeserializer.deserializeFile(bis);

        byte[] original;
        byte[] deserialized;

        ArrayList<byte[]> originalEntries = (ArrayList<byte[]>) originalTable.getColumns().get(0).getData();
        ArrayList<byte[]> deserializedEntries = (ArrayList<byte[]>) deserializedTable.getColumns().get(0).getData();
        for (int j = 0; j < originalEntries.size(); j++) {
            original = originalEntries.get(j);
            deserialized = deserializedEntries.get(j);
            assertArrayEquals(original, deserialized);
        }

        long[] originalLong = (long[]) originalTable.getColumns().get(1).getData();
        long[] deserializedLong = (long[]) deserializedTable.getColumns().get(1).getData();
        assertArrayEquals(originalLong, deserializedLong);
    }
}
