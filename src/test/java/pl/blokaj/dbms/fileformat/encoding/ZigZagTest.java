package pl.blokaj.dbms.fileformat.encoding;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ZigZagTest {
    @Test
    public void testZigZag() {
        long[] data = new long[8192];
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextLong();
        }

        long[] encodedData = ZigZag.encodeZigZag(data);
        long[] decodedData = ZigZag.decodeZigZag(encodedData);
        assertArrayEquals(data, decodedData);
    }
}
