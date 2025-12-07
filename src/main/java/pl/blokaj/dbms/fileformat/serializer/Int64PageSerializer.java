package pl.blokaj.dbms.fileformat.serializer;

import pl.blokaj.dbms.columntype.Int64ColumnPage;

import java.io.IOException;
import java.io.OutputStream;

import static pl.blokaj.dbms.fileformat.encoding.Delta.encodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeSingleVLQ;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.encodeZigZag;

public class Int64PageSerializer implements ColumnPageSerializer<Int64ColumnPage> {
    private Int64PageSerializer() {}
    public static final Int64PageSerializer INSTANCE = new Int64PageSerializer();

    @Override
    public Boolean toFile(Int64ColumnPage column, OutputStream out) throws IOException {
        long[] data = column.getData();
        encodeSingleVLQ(data.length, out);
        byte[] encodedData = encodeVLQ(encodeZigZag(encodeDelta(data)));
        out.write(encodedData);
        return true;
    }


}