package pl.blokaj.dbms.fileformat.encoding;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class DeltaTest {

    @Test
    public void testDeltaEncoding() {
        long[] data = new long[8192];
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextLong();
        }

        long[] encodedData = Delta.encodeDelta(data);
        long[] decodedData = Delta.decodeDelta(encodedData);
        assertArrayEquals(data, decodedData);
    }
}
