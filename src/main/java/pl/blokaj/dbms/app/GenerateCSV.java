package pl.blokaj.dbms.app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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



    public static void main(String[] args) throws IOException {
        generateOnlyInt(8192);
    }
}
