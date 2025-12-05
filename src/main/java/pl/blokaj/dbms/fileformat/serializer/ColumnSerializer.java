package pl.blokaj.dbms.fileformat.serializer;
import pl.blokaj.dbms.columntype.ColumnPage;

import java.io.IOException;
import java.io.OutputStream;

public interface ColumnSerializer<T extends ColumnPage> {

    void toFile(T column, OutputStream out) throws IOException;
}
