package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.serializer.VarcharPageSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VarcharColumnPage extends ColumnPage {
    private final ArrayList<byte[]> entries;

    public VarcharColumnPage(ArrayList<byte[]> entries) {
        this.entries = entries;
    }

    public VarcharColumnPage(List<String> rawData) {
        this.entries = new ArrayList<>(rawData.size());
        for (String data: rawData) {
            this.entries.add(Arrays.copyOf(data.getBytes(StandardCharsets.UTF_8), data.length() + 1));
        }
    }

    public ArrayList<byte[]> getEntries() {
        return entries;
    }

    public ArrayList<byte[]> getData() {
        return entries;
    }

    @Override
    public int getRowCount() {
        return entries.size();
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

    @Override
    public String toString() {
        ArrayList<String> strings = new ArrayList<>();
        entries.forEach(bytes -> strings.add(new String(bytes, StandardCharsets.US_ASCII)));
        return strings.toString();
    }
}