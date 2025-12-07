package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.serializer.Int64PageSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class Int64ColumnPage extends ColumnPage {
    private final long[] data;


    public Int64ColumnPage(long[] data) {
        this.data = data;
    }

    public long[] getData() {
        return data;
    }

    public void serialize(OutputStream stream) throws IOException {
        Int64PageSerializer.INSTANCE.toFile(this, stream);
    }

    public String calculateMetric() {
        return ("Avg: " + Arrays.stream(data).average().orElse(0L));
    }
}
