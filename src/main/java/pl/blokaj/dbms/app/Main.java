package pl.blokaj.dbms.app;

import pl.blokaj.dbms.Table.Table;
import pl.blokaj.dbms.fileformat.deserializer.FileDeserializer;
import pl.blokaj.dbms.fileformat.serializer.FileSerializer;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private final static String resourcePath = "src/main/resources/";


    public static void main(String[] args) throws IOException {
        String fileName = "example.csv";
        GenerateCSV.generateExampleCSV(resourcePath + fileName, 8192, 3, 2);

        Table table = ProcessCSV.processCSV(resourcePath + fileName, 8192, 3, 2);

        System.out.println("Metrics on raw data from CSV");
        for (int i = 0; i < table.getColumns().size(); i++) {
            System.out.println("Column " + (i + 1) + " metric: " + table.getColumns().get(i).calculateMetric());
        }

        String serializedName = "example.dbms";
        FileSerializer.toFile(resourcePath + serializedName, table);

        System.out.println("Metrics on serialized data from DBMS");
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(resourcePath + serializedName));) {
            Table deserialized = FileDeserializer.deserializeFile(fis);
            for (int i = 0; i < deserialized.getColumns().size(); i++) {
                System.out.println("Column " + (i + 1) + " metric: " + deserialized.getColumns().get(i).calculateMetric());
            }
        }

        System.out.println("CSV size: " + Files.size(Path.of(resourcePath + fileName)));
        System.out.println("DBMS size: " + Files.size(Path.of(resourcePath + serializedName)));

    }

}
