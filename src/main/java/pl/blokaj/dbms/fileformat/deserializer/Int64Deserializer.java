package pl.blokaj.dbms.fileformat.deserializer;

import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.Int64Column;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.Int64ColumnHeader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static pl.blokaj.dbms.fileformat.encoding.VLQ.decodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.Delta.decodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.decodeZigZag;

public class Int64Deserializer implements ColumnDeserializer<Int64Column> {
    private Int64Deserializer() {}
    public static final Int64Deserializer INSTANCE = new Int64Deserializer();

    @Override
    public Column deserialize(InputStream in) throws IOException {
        Int64ColumnHeader header = new Int64ColumnHeader();
        header.setDataLength(VLQ.decodeSingleVLQ(in));
        return new Int64Column(decodeZigZag(decodeVLQ(in, (int) header.getDataLength())));
    }
}
