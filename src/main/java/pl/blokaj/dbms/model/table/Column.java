package pl.blokaj.dbms.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

/**
 * Description of single column in table
 */
public class Column {
    @Nonnull
    @JsonProperty("name")
    private String name;
    @Nonnull
    @JsonProperty("type")
    private LogicalColumnType type;

    public Column(){}
    public Column(@Nonnull String name, @Nonnull LogicalColumnType type) {
        this.name = name;
        this.type = type;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public LogicalColumnType getType() {
        return type;
    }

    public void setType(@Nonnull LogicalColumnType type) {
        this.type = type;
    }
}
