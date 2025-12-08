package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.queryservice.QueryType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description of a select query (extension in project no 4)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectQuery implements QueryDefinition {
    @JsonProperty("tableName")
    @Nonnull
    private String tableName;

    public SelectQuery() {}
    public static List<MultipleProblemsError.Problem> validateQuery(JsonNode node, Metastore m) {
        List<MultipleProblemsError.Problem> problems = new ArrayList<>();
        String name = node.get("tableName").asText();
        if (m.getTableUuid(name) == null) {
            problems.add(new MultipleProblemsError.Problem("Table of that name does not exist", name));
        }
        return problems;
    }

    @Override
    @JsonIgnore
    public QueryType getQueryType() {
        return QueryType.SELECT;
    }

    @NotNull
    @Override
    @JsonIgnore
    public String getTableName() {
        return tableName;
    }

    public void setTableName(@Nonnull String tableName) {
        this.tableName = tableName;
    }
}
