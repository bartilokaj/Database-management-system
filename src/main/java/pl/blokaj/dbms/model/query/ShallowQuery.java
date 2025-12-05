package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

/**
 * Description of a shallow representation of a query
 */
public class ShallowQuery {

    /**
     * ID of the query
     */
    @JsonProperty("queryId")
    @Nonnull
    private String queryId;

    /**
     * Status of the query
     */
    @JsonProperty("status")
    @Nonnull
    private QueryStatus status;

    /**
     * Default constructor
     */
    public ShallowQuery() {
    }

    /**
     * Constructor with all required fields
     */
    public ShallowQuery(@Nonnull String queryId, @Nonnull QueryStatus status) {
        this.queryId = queryId;
        this.status = status;
    }

    // Getters and setters

    @NotNull
    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(@Nonnull String queryId) {
        this.queryId = queryId;
    }

    @NotNull
    public QueryStatus getStatus() {
        return status;
    }

    public void setStatus(@Nonnull QueryStatus status) {
        this.status = status;
    }
}

