package pl.blokaj.dbms.queryservice;

import com.fasterxml.jackson.databind.JsonNode;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.query.CopyQuery;
import pl.blokaj.dbms.model.query.SelectQuery;

import java.util.ArrayList;
import java.util.List;

public enum QueryType {
    COPY,
    SELECT,
    NONE;

    public static QueryType getQueryType(JsonNode queryNode) {
        if (queryNode.get("sourceFilepath") != null && queryNode.get("destinationTableName") != null) {
            return COPY;
        }
        else if (queryNode.get("tableName") != null) {
            return SELECT;
        }
        else {
            return NONE;
        }
    }
}
