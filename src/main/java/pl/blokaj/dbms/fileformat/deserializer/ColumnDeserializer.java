package pl.blokaj.dbms.fileformat.deserializer;

import pl.blokaj.dbms.columntype.Column;

import java.io.IOException;
import java.io.InputStream;

public interface ColumnDeserializer<T extends Column> {


    public Column deserialize(InputStream in) throws IOException;
}
