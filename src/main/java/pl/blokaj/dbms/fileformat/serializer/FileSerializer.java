package pl.blokaj.dbms.fileformat.serializer;

import com.github.luben.zstd.ZstdOutputStream;
import pl.blokaj.dbms.tablepage.TablePage;
import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.columntype.ColumnDictionary;
import pl.blokaj.dbms.fileformat.encoding.VLQ;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileSerializer {

    public static void toFile(String fullPath, TablePage tablePage) throws IOException {
        Files.deleteIfExists(Paths.get(fullPath));
        ArrayList<ColumnPage> columnPages = tablePage.getColumns();

        try (ZstdOutputStream bos = new ZstdOutputStream(new FileOutputStream(fullPath))) {
            VLQ.encodeSingleVLQ(columnPages.size(), bos);
            for (ColumnPage columnPage : columnPages) {
                byte type = ColumnDictionary.getColumnType(columnPage.getClass());
                bos.write(type);
                columnPage.serialize(bos);
            }
        }
    }


}
