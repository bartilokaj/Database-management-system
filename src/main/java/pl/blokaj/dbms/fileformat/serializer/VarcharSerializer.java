package pl.blokaj.dbms.fileformat.serializer;

import pl.blokaj.dbms.columntype.VarcharColumnPage;

import java.io.*;
import java.util.ArrayList;

import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeSingleVLQ;

public class VarcharSerializer implements ColumnSerializer<VarcharColumnPage> {
    private VarcharSerializer() {}
    public static final VarcharSerializer INSTANCE = new VarcharSerializer();

    @Override
    public void toFile(VarcharColumnPage column, OutputStream out) throws IOException {
        ArrayList<byte[]> entries = column.getEntries();
        long sum = 0;
        for (byte[] entry: entries) {
            sum += entry.length;
        }

        encodeSingleVLQ(entries.size(), out);
        encodeSingleVLQ(sum, out);
        for  (byte[] entry : entries) {
            out.write(entry);
        }
    }
}
