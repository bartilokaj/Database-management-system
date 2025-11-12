package pl.blokaj.dbms.fileformat.serializer;
import pl.blokaj.dbms.columntype.Column;
import pl.blokaj.dbms.columntype.CompressedColumn;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

public interface ColumnSerializer<T extends Column> {

    public CompressedColumn toMemory(T column);
    Boolean toFile(T column, OutputStream out) throws IOException;
}
