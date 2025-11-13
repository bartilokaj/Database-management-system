package pl.blokaj.dbms.fileformat.serializer;

import com.github.luben.zstd.ZstdOutputStreamNoFinalizer;
import pl.blokaj.dbms.columntype.VarcharColumn;

import java.io.*;
import java.util.ArrayList;

import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeSingleVLQ;

public class VarcharSerializer implements ColumnSerializer<VarcharColumn> {
    private VarcharSerializer() {}
    public static final VarcharSerializer INSTANCE = new VarcharSerializer();

    @Override
    public Boolean toFile(VarcharColumn column, OutputStream out) throws IOException {
        ArrayList<byte[]> entries = column.getEntries();

        long sum = 0;
        for (byte[] entry: entries) {
            sum += entry.length;
        }

        encodeSingleVLQ(entries.size(), out);
        encodeSingleVLQ(sum, out);

        ZstdOutputStreamNoFinalizer output = new ZstdOutputStreamNoFinalizer(out);
        output.setChecksum(false);
        for  (byte[] entry : entries) {
            output.write(entry);
        }
        output.closeWithoutClosingParentStream();
        return true;
    }
}
