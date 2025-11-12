package pl.blokaj.dbms.fileformat.serializer;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdOutputStream;
import com.github.luben.zstd.ZstdOutputStreamNoFinalizer;
import pl.blokaj.dbms.columntype.CompressedVarcharColumn;
import pl.blokaj.dbms.columntype.VarcharColumn;
import pl.blokaj.dbms.fileformat.encoding.VLQ;

import java.io.*;
import java.util.ArrayList;

import static pl.blokaj.dbms.fileformat.encoding.Delta.encodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeSingleVLQ;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeVLQ;

public class VarcharSerializer implements ColumnSerializer<VarcharColumn> {
    private VarcharSerializer() {}
    public static final VarcharSerializer INSTANCE = new VarcharSerializer();

    @Override
    public CompressedVarcharColumn toMemory(VarcharColumn column) {
        return null;
    }

    @Override
    public Boolean toFile(VarcharColumn column, OutputStream out) throws IOException {
        ArrayList<byte[]> entries = column.getEntries();

        encodeSingleVLQ(entries.size(), out);

        ZstdOutputStreamNoFinalizer output = new ZstdOutputStreamNoFinalizer(out);
        for  (byte[] entry : entries) {
            output.write(entry);
        }
        output.closeWithoutClosingParentStream();
        return true;
    }
}
