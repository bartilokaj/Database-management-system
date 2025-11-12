package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.deserializer.ColumnDeserializer;
import pl.blokaj.dbms.fileformat.deserializer.Int64Deserializer;

public abstract class Column {
    public abstract ColumnDeserializer<? extends Column> getDeserializer();
}


