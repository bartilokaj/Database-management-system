package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.serializer.VarcharPageSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;

public final class VarcharColumnPage extends ColumnPage {
    private final ArrayList<byte[]> entries;

    public VarcharColumnPage(ArrayList<byte[]> entries) {
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
        VarcharPageSerializer.INSTANCE.toFile(this, stream);
    }

    @Override
    public String calculateMetric() {
        BigInteger lengthSum = BigInteger.ZERO;
        for (byte[] entry : entries) {
            lengthSum = lengthSum.add(BigInteger.valueOf(entry.length));
        }
        return ("lengthSum: " + lengthSum);
    }
}