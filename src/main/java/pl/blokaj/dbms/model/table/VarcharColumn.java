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
    private List<String> values;

    // Constructors
    public VarcharColumn() {}

    public VarcharColumn(@Nonnull List<String> values) {
        this.values = values;
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
