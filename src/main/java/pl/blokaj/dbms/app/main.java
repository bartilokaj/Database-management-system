package pl.blokaj.dbms.app;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;
import com.github.luben.zstd.ZstdOutputStreamNoFinalizer;
import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.columntype.Int64Column;
import pl.blokaj.dbms.columntype.VarcharColumn;
import pl.blokaj.dbms.fileformat.deserializer.VarcharDeserializer;
import pl.blokaj.dbms.fileformat.serializer.FileSerializer;
import pl.blokaj.dbms.fileformat.serializer.Int64Serializer;
import pl.blokaj.dbms.fileformat.serializer.VarcharSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static pl.blokaj.dbms.fileformat.encoding.Delta.encodeDelta;
import static pl.blokaj.dbms.fileformat.encoding.VLQ.encodeVLQ;
import static pl.blokaj.dbms.fileformat.encoding.ZigZag.encodeZigZag;

public class main {
    private static byte[] stringToBytes(String str) {
        byte[] bytes = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            bytes[i] = (byte) str.charAt(i);
        }
        bytes[str.length()] = '\0';
        return bytes;
    }

    public static void main(String[] args) throws IOException {
//        Table smol = ProcessCSV.smol();
//        FileSerializer.toFile();
    }

}
