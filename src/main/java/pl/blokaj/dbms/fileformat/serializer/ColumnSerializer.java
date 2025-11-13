package pl.blokaj.dbms.fileformat.serializer;
import pl.blokaj.dbms.columntype.Column;

import java.io.IOException;
import java.io.OutputStream;

public interface ColumnSerializer<T extends Column> {

    Boolean toFile(T column, OutputStream out) throws IOException;
}
