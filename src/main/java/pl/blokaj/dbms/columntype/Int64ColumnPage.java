package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.fileformat.serializer.Int64PageSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public final class Int64ColumnPage extends ColumnPage {
    private final long[] data;


    public Int64ColumnPage(long[] data) {
        this.data = data;
    }

    public Int64ColumnPage(List<String> rawData) throws NumberFormatException {
        long[] data = new long[rawData.size()];
        for (int i = 0; i < rawData.size(); i++) {
            data[i] = Long.parseLong(rawData.get(i));
        }
        this.data = data;
    }

    public long[] getData() {
        return data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    public void serialize(OutputStream stream) throws IOException {
        Int64PageSerializer.INSTANCE.toFile(this, stream);
    }

    public String calculateMetric() {
        return ("Avg: " + Arrays.stream(data).average().orElse(0L));
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
