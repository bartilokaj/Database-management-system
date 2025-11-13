package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.deserializer.ColumnDeserializer;
import pl.blokaj.dbms.fileformat.deserializer.Int64Deserializer;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Column {
    public abstract void serialize(OutputStream stream) throws IOException;
    public abstract Object getData();
}


