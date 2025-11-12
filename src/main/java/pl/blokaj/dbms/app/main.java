package pl.blokaj.dbms.app;

import com.github.luben.zstd.ZstdInputStream;
import pl.blokaj.dbms.columntype.Int64Column;
import pl.blokaj.dbms.fileformat.serializer.Int64Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import static pl.blokaj.dbms.fileformat.encoding.Delta.encodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.encodeZigZag;

public class main {
    public static void main(String[] args) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream("avc\0avc".getBytes());
        InputStream in = new ZstdInputStream(System.in);


    }

}
