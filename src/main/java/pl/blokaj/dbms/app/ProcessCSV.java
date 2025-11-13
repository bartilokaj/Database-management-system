package pl.blokaj.dbms.app;

import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.columntype.Int64Column;
import pl.blokaj.dbms.columntype.VarcharColumn;
import pl.blokaj.dbms.fileformat.serializer.FileSerializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProcessCSV {

    private static byte[] stringToBytes(String str) {
        byte[] bytes = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            bytes[i] = (byte) str.charAt(i);
        }
        bytes[str.length()] = '\0';
        return bytes;
    }

    public static Table onlyInt() throws IOException {
        String fileName = "src/main/resources/onlyInt.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            long[] col1 = new long[8192];
            long[] col2 = new long[8192];

            for (int i = 0; i < 8192; i++) {
                line = br.readLine();
                String[] values = line.split(",");
                col1[i] = Long.parseLong(values[0]);
                col2[i] = Long.parseLong(values[1]);
            }

            Table table = new Table();
            table.addColumn(new Int64Column(col1));
            table.addColumn(new Int64Column(col2));
            return table;
        }
    }

    public static Table onlyString() throws IOException {
        String fileName = "src/main/resources/onlyInt.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            ArrayList<byte[]> entries1 = new ArrayList<>();
            ArrayList<byte[]> entries2 = new ArrayList<>();


            for (int i = 0; i < 8192; i++) {
                line = br.readLine();
                String[] values = line.split(",");
                entries1.add(stringToBytes(values[0]));
                entries2.add(stringToBytes(values[1]));
            }

            Table table = new Table();
            table.addColumn(new VarcharColumn(entries1));
            table.addColumn(new VarcharColumn(entries2));
            return table;
        }
    }

    public static Table stringAndInt() throws IOException {
        String fileName = "src/main/resources/onlyInt.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            ArrayList<byte[]> entries1 = new ArrayList<>();
            long[] col2 = new long[8192];


            for (int i = 0; i < 8192; i++) {
                line = br.readLine();
                String[] values = line.split(",");
                entries1.add(stringToBytes(values[0]));
                col2[i] = Long.parseLong(values[1]);
            }

            Table table = new Table();
            table.addColumn(new VarcharColumn(entries1));
            table.addColumn(new Int64Column(col2));
            return table;
        }
    }

    public static Table intAndString() throws IOException {
        String fileName = "src/main/resources/onlyInt.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            ArrayList<byte[]> entries1 = new ArrayList<>();
            long[] col2 = new long[8192];


            for (int i = 0; i < 8192; i++) {
                line = br.readLine();
                String[] values = line.split(",");
                entries1.add(stringToBytes(values[1]));
                col2[i] = Long.parseLong(values[0]);
            }

            Table table = new Table();
            table.addColumn(new Int64Column(col2));
            table.addColumn(new VarcharColumn(entries1));
            return table;
        }
    }

    public static Table smol() throws IOException {
        ArrayList<byte[]> entries1 = new ArrayList<>();
        ArrayList<byte[]> entries2 = new ArrayList<>();

        entries1.add(stringToBytes("smol123"));
        entries2.add(stringToBytes("smol213"));

        Table table = new Table();
        table.addColumn(new VarcharColumn(entries1));
        table.addColumn(new VarcharColumn(entries2));
        return table;
    }

    public static void main(String[] args) throws IOException {
        Table onlyInt = onlyInt();
        FileSerializer.toFile("dat1.dbms", onlyInt);
        Table onlyString = onlyString();
        FileSerializer.toFile("onlyString.dbms", onlyString);
        Table smol  = smol();
        FileSerializer.toFile("smol.dbms", smol);
        Table stringAndInt = stringAndInt();
        FileSerializer.toFile("stringAndInt.dbms", stringAndInt);
        Table  intAndString = intAndString();
        FileSerializer.toFile("intAndString.dbms", intAndString);
    }
}
