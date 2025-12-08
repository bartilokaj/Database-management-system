package pl.blokaj.dbms.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.columntype.Int64ColumnPage;

import java.util.Arrays;
import java.util.List;

/**
 * Column containing INT64 values
 */
public class Int64Column implements ColumnBase {
    @Nonnull
    @JsonProperty("Int64Column")
    private List<Long> values;

    // Constructors
    public Int64Column() {}

    public Int64Column(@Nonnull List<Long> values) {
        this.values = values;
    }

    @Override
    public ColumnBase sliceColumn(int limit) {
        return new Int64Column(values.subList(0, limit));
    }

    @Override
    public void addNextPage(ColumnPage columnPage) {
        Int64ColumnPage col = (Int64ColumnPage) columnPage;
        values.addAll(Arrays.stream(col.getData()).boxed().toList());
    }

    // Getter and Setter
    @Nonnull
    public List<Long> getValues() {
        return values;
    }

    public void setValues(@Nonnull List<Long> values) {
        this.values = values;
    }
}
