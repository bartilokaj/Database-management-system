package pl.blokaj.dbms.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Description of the table in the database
 */
public class TableSchema {

    /**
     * Name of the table
     */
    @JsonProperty("name")
    @Nonnull
    private String name;

    /**
     * List of columns in the table
     */
    @JsonProperty("columns")
    @Nonnull
    private List<Column> columns;

    /**
     * Default constructor
     */
    public TableSchema() {
    }

    /**
     * Constructor with all required fields
     */
    public TableSchema(@Nonnull String name, @Nonnull List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getName().equals(columnName)) return i;
        }
        return -1;
    }

    // Getters and setters

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @NotNull
    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(@Nonnull List<Column> columns) {
        this.columns = columns;
    }
}
