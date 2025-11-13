package pl.blokaj.dbms.fileformat.deserializer;

import pl.blokaj.dbms.columntype.VarcharColumn;
import pl.blokaj.dbms.fileformat.encoding.VLQ;
import pl.blokaj.dbms.fileformat.headers.VarcharColumnHeader;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles deserializing varchar columns
 */
public class VarcharDeserializer implements ColumnDeserializer<VarcharColumn> {
    private VarcharDeserializer() {}
    public static final VarcharDeserializer INSTANCE = new VarcharDeserializer();

    /**
     * Reads the input stream and looks for '\0' separator
     * Does not do any decoding because Zstd compression being applied to the whole file due to technical reasons
     * (Zstd adds some data to the end of the frame, and I did not find any solution to deal with it)
     * @param in - Zstd input stream
     * @return deserialized column
     */
    @Override
    public VarcharColumn deserialize(InputStream in) throws IOException {
        VarcharColumnHeader header = new VarcharColumnHeader();
        header.setColumnSize(VLQ.decodeSingleVLQ(in));
        header.setDataSize(VLQ.decodeSingleVLQ(in));
        ArrayList<byte[]> entries = new ArrayList<>((int) header.getColumnSize());


        byte[] buff = new byte[1024];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bytesRead;
        long bytesLeft = header.getDataSize();
        int toRead = Math.min(1024, (int) bytesLeft);
        while ((bytesRead = in.read(buff, 0, toRead)) != -1 && toRead > 0) {
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
        output.close();
        return new VarcharColumn(entries);
    }
}
