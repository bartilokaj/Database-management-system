package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nonnull;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.error.MultipleProblemsError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description of a select query (extension in project no 4)
 */
public class SelectQuery implements QueryDefinition {
    @JsonProperty("tableName")
    @Nonnull
    private String tableName;

    public static List<MultipleProblemsError.Problem> validateQuery(JsonNode node, Metastore m) {
        List<MultipleProblemsError.Problem> problems = new ArrayList<>();
        String name = node.get("tableName").asText();
        if (m.getTableUuid(name) == null) {
            problems.add(new MultipleProblemsError.Problem("Table of that name does not exist", name));
        }
        return problems;
    }
}
