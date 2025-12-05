package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

/**
 * Description of a select query (extension in project no 4)
 */
public class SelectQuery implements QueryDefinition {
    @JsonProperty("tableName")
    @Nonnull
    private String tableName;
}
