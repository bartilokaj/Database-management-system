package pl.blokaj.dbms.fileformat.encoding;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.decodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeVLQ;

public class VLQTest {
    @Test
    public void testVLQ(){
        long[] data = new long[8192];
        Random random = new Random();
        for  (int i = 0; i < data.length; i++) {
            data[i] = random.nextLong();
        }

        long[] result = decodeVLQ(encodeVLQ(data), data.length);
        assertArrayEquals(data, result);
    }
}
