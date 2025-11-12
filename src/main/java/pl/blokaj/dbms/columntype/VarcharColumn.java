package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.deserializer.ColumnDeserializer;
import pl.blokaj.dbms.fileformat.deserializer.Int64Deserializer;
import pl.blokaj.dbms.fileformat.deserializer.VarcharDeserializer;
import pl.blokaj.dbms.fileformat.headers.VarcharColumnHeader;

import java.util.ArrayList;

public final class VarcharColumn extends Column {
    private final ArrayList<byte[]> entries;

    public VarcharColumn(ArrayList<byte[]> entries) {
        this.entries = entries;
    }

    public ArrayList<byte[]> getEntries() {
        return entries;
    }

    @Override
    public ColumnDeserializer<? extends Column> getDeserializer() {
        return VarcharDeserializer.INSTANCE;
    }
}