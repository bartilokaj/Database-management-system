package pl.blokaj.dbms.fileformat.deserializer;

import org.junit.jupiter.api.Test;
import pl.blokaj.dbms.columntype.VarcharColumnPage;
import pl.blokaj.dbms.fileformat.serializer.VarcharPageSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class VarcharDeserializerTest {

    private byte[] stringToBytes(String str) {
        byte[] bytes = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            bytes[i] = (byte) str.charAt(i);
        }
        bytes[str.length()] = '\0';
        return bytes;
    }

    @Test
    public void SimpleArrayTest() throws IOException {
        ArrayList<byte[]> entries = new ArrayList<>();
        byte[] data = new byte[]{1,2,3,4,5,6,7,8,9,'\0'};
        entries.add(data);
        entries.add(new byte[]{1,2,3,4,5,6,7,8,9,'\0'});
        entries.add(new byte[]{9,8,7,6,5,4,3,2,1,'\0'});
        VarcharColumnPage col = new VarcharColumnPage(entries);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        VarcharPageSerializer.INSTANCE.toFile(col, out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        VarcharColumnPage col2 = VarcharPageDeserializer.INSTANCE.deserialize(in);
        assertArrayEquals(new byte[]{1,2,3,4,5,6,7,8,9,'\0'}, col2.getEntries().getFirst());
        assertArrayEquals(new byte[]{1,2,3,4,5,6,7,8,9,'\0'}, col2.getEntries().get(1));
        assertArrayEquals(new byte[]{9,8,7,6,5,4,3,2,1,'\0'}, col2.getEntries().get(2));
    }

    @Test
    public void zeroByteTest() throws IOException {
        byte[] og = {'a','b','\0'};
        assertArrayEquals(og, stringToBytes("ab"));
    }

    @Test
    public void textTest() throws IOException {
        ArrayList<byte[]> entries = new ArrayList<>();
        entries.add(stringToBytes("this project is sick"));
        entries.add(stringToBytes("dont think so"));
        VarcharColumnPage col = new VarcharColumnPage(entries);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        VarcharPageSerializer.INSTANCE.toFile(col, out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        VarcharColumnPage col2 = VarcharPageDeserializer.INSTANCE.deserialize(in);

        assertArrayEquals(stringToBytes("this project is sick"),  col2.getEntries().getFirst());
        assertArrayEquals(stringToBytes("dont think so"),  col2.getEntries().get(1));
    }
}
