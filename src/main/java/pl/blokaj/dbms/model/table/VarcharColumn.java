package pl.blokaj.dbms.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Column containing VARCHAR values
 */
public class VarcharColumn implements ColumnBase {
    @Nonnull
    @JsonProperty("VarcharColumn")
    private List<byte[]> values;

    // Constructors
    public VarcharColumn() {}

    public VarcharColumn(@Nonnull List<byte[]> values) {
        this.values = values;
    }

    @Override
    public ColumnBase sliceColumn(int limit) {
        return new VarcharColumn(values.subList(0, limit));
    }

    // Getter and Setter
    @Nonnull
    public List<byte[]> getValues() {
        return values;
    }

    public void setValues(@Nonnull List<byte[]> values) {
        this.values = values;
    }
}
