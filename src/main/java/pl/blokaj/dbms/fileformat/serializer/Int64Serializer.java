package pl.blokaj.dbms.fileformat.serializer;

import pl.blokaj.dbms.columntype.Int64ColumnPage;

import java.io.IOException;
import java.io.OutputStream;

import static pl.blokaj.dbms.fileformat.encoding.Delta.encodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeSingleVLQ;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.encodeZigZag;

public class Int64Serializer implements ColumnSerializer<Int64ColumnPage> {
    private Int64Serializer() {}
    public static final Int64Serializer INSTANCE = new Int64Serializer();

    @Override
    public void toFile(Int64ColumnPage column, OutputStream out) throws IOException {
        long[] data = column.getData();
        encodeSingleVLQ(data.length, out);
        byte[] encodedData = encodeVLQ(encodeZigZag(encodeDelta(data)));
        out.write(encodedData);
    }
}