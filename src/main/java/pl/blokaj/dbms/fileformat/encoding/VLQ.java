package pl.blokaj.dbms.fileformat.encoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handles VLQ encoding for arrays and single longs
 */
public class VLQ {
    private static final int DATA_MASK = 0x7F; // 0111 1111
    private static final int CONTINUATION_BIT = 0x80;

    public static void encodeSingleVLQ(long value, OutputStream out) throws IOException {
        while ((value & ~DATA_MASK) != 0) {
            out.write((int) ((value & DATA_MASK) | CONTINUATION_BIT));
            value >>>= 7;
        }
        out.write((int)(value & DATA_MASK));
    }

    public static long decodeSingleVLQ(InputStream in) throws IOException {
        int shift = 0;
        long value = 0;
        while (true) {
            byte b = (byte) in.read();
            value |= (long) (b & DATA_MASK) << shift;
            if ((b & CONTINUATION_BIT) == 0) break;
            shift += 7;
        }
        return value;
    }

    public static byte[] encodeVLQ(long[] column) throws RuntimeException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (long v : column) {
                encodeSingleVLQ(v, out);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long[] decodeVLQ(InputStream in, int columnSize) throws IOException {
        long[] column = new long[columnSize];
        int dataIndex = 0;
        for (int i = 0; i < columnSize; i++) {
            column[i] = decodeSingleVLQ(in);
        }
        return column;
    }
}
