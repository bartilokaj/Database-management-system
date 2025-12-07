package pl.blokaj.dbms.fileformat.serializer;

import pl.blokaj.dbms.columntype.VarcharColumnPage;

import java.io.*;
import java.util.ArrayList;

import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeSingleVLQ;

public class VarcharPageSerializer implements ColumnPageSerializer<VarcharColumnPage> {
    private VarcharPageSerializer() {}
    public static final VarcharPageSerializer INSTANCE = new VarcharPageSerializer();

    @Override
    public Boolean toFile(VarcharColumnPage column, OutputStream out) throws IOException {
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
        return true;
    }
}
