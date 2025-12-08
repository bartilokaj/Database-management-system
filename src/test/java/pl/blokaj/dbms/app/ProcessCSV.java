package pl.blokaj.dbms.app;

import pl.blokaj.dbms.fileformat.serializer.FileSerializer;
import pl.blokaj.dbms.tablepage.TablePage;
import pl.blokaj.dbms.columntype.Int64ColumnPage;
import pl.blokaj.dbms.columntype.VarcharColumnPage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public static TablePage onlyInt() throws IOException {
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

            TablePage tablePage = new TablePage();
            tablePage.addColumn(new Int64ColumnPage(col1));
            tablePage.addColumn(new Int64ColumnPage(col2));

            FileSerializer.toFile("src/main/resources/onlyInt.dbms", tablePage);
            return tablePage;
        }
    }

    public static TablePage onlyString() throws IOException {
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

            TablePage tablePage = new TablePage();
            tablePage.addColumn(new VarcharColumnPage(entries1));
            tablePage.addColumn(new VarcharColumnPage(entries2));

            FileSerializer.toFile("src/main/resources/onlyString.dbms", tablePage);
            return tablePage;
        }
    }

    public static TablePage stringAndInt() throws IOException {
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

            TablePage tablePage = new TablePage();
            tablePage.addColumn(new VarcharColumnPage(entries1));
            tablePage.addColumn(new Int64ColumnPage(col2));

            FileSerializer.toFile("src/main/resources/stringAndInt.dbms", tablePage);
            return tablePage;
        }
    }

    public static TablePage intAndString() throws IOException {
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

            TablePage tablePage = new TablePage();
            tablePage.addColumn(new Int64ColumnPage(col2));
            tablePage.addColumn(new VarcharColumnPage(entries1));

            FileSerializer.toFile("src/main/resources/intAndString.dbms", tablePage);
            return tablePage;
        }
    }

    public static TablePage processCSV(String filePath, int size, int int64Columns, int varcharColumns) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            long[][] longs = new long[int64Columns][size];
            ArrayList<byte[]>[] varchars = new ArrayList[varcharColumns];
            for (int i = 0; i < varcharColumns; i++) {
                varchars[i] = new ArrayList<>();
            }

            for (int i = 0; i < size; i++) {
                line = br.readLine();
                String[] values = line.split(",");
                for (int j = 0; j < int64Columns; j++) {
                    longs[j][i] = Long.parseLong(values[j]);
                }
                for (int j = 0; j < varcharColumns; j++) {
                    varchars[j].add(stringToBytes(values[j]));
                }
            }

            TablePage tablePage = new TablePage();
            for (int i = 0; i < int64Columns; i++) {
                tablePage.addColumn(new Int64ColumnPage(longs[i]));
            }
            for (int i = 0; i < varcharColumns; i++) {
                tablePage.addColumn(new VarcharColumnPage(varchars[i]));
            }
            return tablePage;
        }
    }
}
