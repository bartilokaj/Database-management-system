package pl.blokaj.dbms.fileformat.deserializer;

import com.github.luben.zstd.ZstdInputStream;
import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.VarcharColumn;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.VarcharColumnHeader;

import java.io.*;
import java.util.ArrayList;

public class VarcharDeserializer implements ColumnDeserializer<VarcharColumn> {
    private VarcharDeserializer() {}
    public static final VarcharDeserializer INSTANCE = new VarcharDeserializer();

    @Override
    public Column deserialize(InputStream in) throws IOException {
        VarcharColumnHeader header = new VarcharColumnHeader();
        header.setColumnSize(VLQ.decodeSingleVLQ(in));
        header.setDataSize(VLQ.decodeSingleVLQ(in));

        ArrayList<byte[]> entries = new ArrayList<>((int) header.getColumnSize());
        byte[] buff = new byte[1024];

        InputStream nonCLosingIn = new FilterInputStream(in) {
            @Override
            public void close() throws IOException {
                // nothing
            }
        };

        ZstdInputStream input = new ZstdInputStream(nonCLosingIn);
        long bytesRead = 0;
        long bytesToRead = Math.min(header.getDataSize() - bytesRead, 1024L);
        while (input.read(buff, 0, 1024) != -1 && bytesRead < header.getColumnSize()) {

        }
//
//        for (int i = 0; i < header.getColumnSize(); i++) {
//            while ((b = input.read()) != -1) {
//                byteArrayOutputStream.write(b);
//                if (b == '\0') {
//                    entries.add(byteArrayOutputStream.toByteArray());
//                    byteArrayOutputStream.reset();
//                    break;
//                }
//            }
//            if (b == -1) {
//                throw new IOException();
//            }
//        }
//        byteArrayOutputStream.close();
        input.close();

        return new VarcharColumn(entries);
    }
}
