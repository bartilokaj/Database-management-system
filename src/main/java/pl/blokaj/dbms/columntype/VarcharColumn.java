package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.deserializer.ColumnDeserializer;
import pl.blokaj.dbms.fileformat.deserializer.Int64Deserializer;
import pl.blokaj.dbms.fileformat.deserializer.VarcharDeserializer;
import pl.blokaj.dbms.fileformat.headers.VarcharColumnHeader;
import pl.blokaj.dbms.fileformat.serializer.Int64Serializer;
import pl.blokaj.dbms.fileformat.serializer.VarcharSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public final class VarcharColumn extends Column {
    private final ArrayList<byte[]> entries;

    public VarcharColumn(ArrayList<byte[]> entries) {
        this.entries = entries;
    }

    public ArrayList<byte[]> getEntries() {
        return entries;
    }

    public ArrayList<byte[]> getData() {
        return entries;
    }

    @Override
    public void serialize(OutputStream stream) throws IOException {
        VarcharSerializer.INSTANCE.toFile(this, stream);
    }
}