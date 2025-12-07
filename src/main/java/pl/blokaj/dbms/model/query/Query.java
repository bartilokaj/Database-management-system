package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.annotation.Nonnull;


public class Query {
    @Nonnull
    @JsonProperty("queryId")
    private String queryId;

    @Nonnull
    @JsonProperty("status")
    private volatile QueryStatus status;

    @JsonProperty("isResultAvailable")
    private volatile boolean isResultAvailable;

    @JsonProperty("queryDefinition")
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type" // a type discriminator in JSON
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SelectQuery.class, name = "select"),
            @JsonSubTypes.Type(value = CopyQuery.class, name = "copy")
    })
    private QueryDefinition queryDefinition;

    // Constructors
    public Query() { }

    public Query(@Nonnull String queryId, @Nonnull QueryStatus status, boolean isResultAvailable, QueryDefinition queryDefinition) {
        this.queryId = queryId;
        this.status = status;
        this.isResultAvailable = isResultAvailable;
        this.queryDefinition = queryDefinition;
    }

    // Getters and setters
    public String getQueryId() { return queryId; }
    public void setQueryId(String queryId) { this.queryId = queryId; }

    public QueryStatus getStatus() { return status; }
    public void setStatus(QueryStatus status) { this.status = status; }

    public boolean isResultAvailable() { return isResultAvailable; }
    public void setResultAvailable(boolean resultAvailable) { isResultAvailable = resultAvailable; }

    public QueryDefinition getQueryDefinition() { return queryDefinition; }
    public void setQueryDefinition(QueryDefinition queryDefinition) { this.queryDefinition = queryDefinition; }

    // Optionally: toString, equals, hashCode
}
