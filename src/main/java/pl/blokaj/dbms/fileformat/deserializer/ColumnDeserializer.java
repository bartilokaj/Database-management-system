package pl.blokaj.dbms.fileformat.deserializer;

import pl.blokaj.dbms.columntype.ColumnPage;

import java.io.IOException;
import java.io.InputStream;

public interface ColumnDeserializer<T extends ColumnPage> {

    public ColumnPage deserialize(InputStream in) throws IOException;
}
