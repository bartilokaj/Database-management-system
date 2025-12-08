package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.blokaj.dbms.queryservice.QueryType;

public interface QueryDefinition {
    @JsonIgnore
    public QueryType getQueryType();
    @JsonIgnore
    public String getTableName();
}
