package pl.blokaj.dbms.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

/**
 * Description of a shallow representation of a table (e.g. without detailed column information)
 */
public class ShallowTable {

    /**
     * ID of the table
     */
    @JsonProperty("tableId")
    private String tableId;

    /**
     * Name of the table
     */
    @JsonProperty("name")
    @Nonnull
    private String name;

    /**
     * Default constructor
     */
    public ShallowTable() {
    }

    /**
     * Constructor with required fields
     */
    public ShallowTable(@Nonnull String name, String uuid) {
        this.name = name;
        this.tableId = uuid;
    }

    // Getters and setters

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }
}

