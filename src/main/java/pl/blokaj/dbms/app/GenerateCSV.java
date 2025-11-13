package pl.blokaj.dbms.app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GenerateCSV {

    public static void generateOnlyInt(int size) throws IOException {
        List<String[]> data = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            data.add(new String[]{String.valueOf(rand.nextInt(-10000, 1000)),
                    String.valueOf(rand.nextInt(-10000, 10000))});
        }

        try (FileWriter writer = new FileWriter("src/main/resources/onlyInt.csv")) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        }
    }

    public static void generateExampleCSV(String filePath, int size, int int64Columns, int varcharColumns) throws IOException {
        List<String[]> data = new ArrayList<>();
        Random rand = new Random();
//        String[] columnTypes =  new String[int64Columns +  varcharColumns];
//        for (int i = 0; i < int64Columns; i++) {
//            columnTypes[i] = "Int64";
//        }
//        for (int i = int64Columns; i < varcharColumns + int64Columns; i++) {
//            columnTypes[i] = "Varchar";
//        }
//        data.add(columnTypes);
        for (int i = 0; i <= size; i++) {
            String[] row = new String[int64Columns + varcharColumns];
            for (int j = 0; j < int64Columns; j++) {
                row[j] = String.valueOf(rand.nextInt(-10000000, 1000000));
            }
            for (int j = int64Columns; j < varcharColumns + int64Columns; j++) {
                row[j] = UUID.randomUUID().toString();
            }
            data.add(row);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        }
    }
}
