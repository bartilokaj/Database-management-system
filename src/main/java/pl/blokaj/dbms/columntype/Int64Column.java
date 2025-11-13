package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.serializer.Int64Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class Int64Column extends Column {
    private final long[] data;


    public Int64Column(long[] data) {
        this.data = data;
    }

    public long[] getData() {
        return data;
    }

    public void serialize(OutputStream stream) throws IOException {
        Int64Serializer.INSTANCE.toFile(this, stream);
    }

    public String calculateMetric() {
        return ("Avg: " + Arrays.stream(data).average().orElse(0L));
    }
}
