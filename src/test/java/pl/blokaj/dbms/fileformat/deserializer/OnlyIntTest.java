package pl.blokaj.dbms.fileformat.deserializer;

import org.junit.jupiter.api.Test;
import pl.blokaj.dbms.tablepage.TablePage;
import pl.blokaj.dbms.app.ProcessCSV;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class OnlyIntTest {

    @Test
    public void OnlyIntTest() throws IOException {
        TablePage originalTablePage = ProcessCSV.onlyInt();
        String filePath = "src/main/resources/onlyInt.dbms";

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        TablePage deserializedTablePage = FileDeserializer.deserializeFile(bis);

        long[] original;
        long[] deserialized;

        for (int i = 0; i < originalTablePage.getColumns().size(); i++) {
            original = (long[]) originalTablePage.getColumns().get(i).getData();
            deserialized = (long[]) deserializedTablePage.getColumns().get(i).getData();
            assertArrayEquals(original, deserialized);
        }
    }
}
