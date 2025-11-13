package pl.blokaj.dbms.fileformat.deserializer;

import com.github.luben.zstd.ZstdInputStream;
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.Vector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;
import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.VarcharColumn;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.VarcharColumnHeader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VarcharDeserializer implements ColumnDeserializer<VarcharColumn> {
    private VarcharDeserializer() {}
    public static final VarcharDeserializer INSTANCE = new VarcharDeserializer();

    @Override
    public VarcharColumn deserialize(InputStream in) throws IOException {
        System.out.println("Varchar start");
        VarcharColumnHeader header = new VarcharColumnHeader();
        header.setColumnSize(VLQ.decodeSingleVLQ(in));
        header.setDataSize(VLQ.decodeSingleVLQ(in));
        ArrayList<byte[]> entries = new ArrayList<>((int) header.getColumnSize());


        InputStream nonCLosingIn = new FilterInputStream(in) {
            @Override
            public void close() throws IOException {
                // nothing
            }
        };

        ZstdInputStream input = new ZstdInputStream(nonCLosingIn);
        byte[] buff = new byte[1024];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bytesRead;
        long bytesLeft = header.getDataSize();
        int toRead = Math.min(1024, (int) bytesLeft);
        while ((bytesRead = input.read(buff, 0, toRead)) != -1 && toRead > 0) {
            for (int i = 0; i < bytesRead; i++) {
                output.write(buff[i]);
                if (buff[i] == '\0') {
                    entries.add(output.toByteArray());
                    output.reset();
                }
            }
            bytesLeft -= bytesRead;
            toRead = Math.min(1024, (int) bytesLeft);
        }
        System.out.println("bytes left: " + bytesLeft);
        output.close();
        input.close();

        return new VarcharColumn(entries);
    }
}
