package pl.blokaj.dbms.fileformat.serializer;

import com.github.luben.zstd.ZstdOutputStream;
import pl.blokaj.dbms.tablepage.TablePage;
import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.columntype.ColumnDictionary;
import pl.blokaj.dbms.fileformat.encoding.VLQ;

import java.io.*;
import java.util.ArrayList;

public class FileSerializer {

    public static void toFile(String filePath, TablePage tablePage) throws IOException {
        ArrayList<ColumnPage> columnPages = tablePage.getColumns();

        try (ZstdOutputStream bos = new ZstdOutputStream(new FileOutputStream(filePath))) {
            VLQ.encodeSingleVLQ(columnPages.size(), bos);
            for (ColumnPage columnPage : columnPages) {
                byte typeByte = ColumnDictionary.getColumnType(columnPage.getClass());
                bos.write(typeByte);
                columnPage.serialize(bos);
            }
        }
    }


}
