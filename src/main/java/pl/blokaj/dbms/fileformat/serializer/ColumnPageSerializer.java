package pl.blokaj.dbms.fileformat.serializer;
import pl.blokaj.dbms.columntype.ColumnPage;

import java.io.IOException;
import java.io.OutputStream;

public interface ColumnPageSerializer<T extends ColumnPage> {

    Boolean toFile(T column, OutputStream out) throws IOException;
}
