package pl.blokaj.dbms.fileformat.encoding;

import com.github.luben.zstd.Zstd;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static pl.blokaj.dbms.fileformat.encoding.Delta.decodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.Delta.encodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.decodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.decodeZigZag;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.encodeZigZag;

public class FullIntEncodingTest {
    @Test
    public void testDeltaAndVLQ() {
        long[] data = new long[8192];
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextLong();
        }

        byte[] encodedData = encodeVLQ(encodeZigZag(encodeDelta(data)));
        byte[] compressedData = Zstd.compress(encodedData);
        int originalSize = encodedData.length;

        byte[] decompressedData = Zstd.decompress(compressedData, originalSize);
        long[] decodedData = decodeDelta(decodeZigZag(decodeVLQ(decompressedData, 8192)));
        assertArrayEquals(data, decodedData);
    }
}
