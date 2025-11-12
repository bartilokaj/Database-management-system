package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.deserializer.ColumnDeserializer;
import pl.blokaj.dbms.fileformat.deserializer.Int64Deserializer;

public final class Int64Column extends Column {
    private final long[] data;


    public Int64Column(long[] data) {
        this.data = data;
    }

    public long[] getData() {
        return data;
    }


    @Override
    public ColumnDeserializer<? extends Column> getDeserializer() {
        return Int64Deserializer.INSTANCE;
    }
}
