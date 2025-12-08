package pl.blokaj.dbms.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.columntype.VarcharColumnPage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Column containing VARCHAR values
 */
public class VarcharColumn implements ColumnBase {
    @Nonnull
    @JsonProperty("VarcharColumn")
    private List<String> values;

    // Constructors
    public VarcharColumn() {}

    public VarcharColumn(@Nonnull List<String> values) {
        this.values = values;
    }

    @Override
    public ColumnBase sliceColumn(int limit) {
        return new VarcharColumn(values.subList(0, limit));
    }

    @Override
    public void addNextPage(ColumnPage columnPage) {
        VarcharColumnPage col = (VarcharColumnPage) columnPage;
        for (byte[] data : col.getData()) {
            String s = new String(data, StandardCharsets.UTF_8);
            if (!s.isEmpty()) {
                s = s.substring(0, s.length() - 1);
            }
            values.add(s);
        }
    }

    // Getter and Setter
    @Nonnull
    public List<String> getValues() {
        return values;
    }

    public void setValues(@Nonnull List<String> values) {
        this.values = values;
    }
}
