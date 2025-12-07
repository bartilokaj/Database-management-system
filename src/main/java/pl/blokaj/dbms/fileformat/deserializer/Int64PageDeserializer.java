package pl.blokaj.dbms.fileformat.deserializer;

import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.columntype.Int64ColumnPage;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.Int64ColumnPageHeader;

import java.io.IOException;
import java.io.InputStream;

import static pl.blokaj.dbms.fileformat.encoding.VLQ.decodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.Delta.decodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.decodeZigZag;

/**
 * Handles deserializing int64 column
 */
public class Int64PageDeserializer implements ColumnPageDeserializer<Int64ColumnPage> {
    private Int64PageDeserializer() {}
    public static final Int64PageDeserializer INSTANCE = new Int64PageDeserializer();

    @Override
    public ColumnPage deserialize(InputStream in) throws IOException {
        Int64ColumnPageHeader header = new Int64ColumnPageHeader();
        header.setDataLength(VLQ.decodeSingleVLQ(in));
        return new Int64ColumnPage(decodeDelta(decodeZigZag(decodeVLQ(in, (int) header.getDataLength()))));
    }
}
