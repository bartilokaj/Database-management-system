package pl.blokaj.dbms.queryservice;

import com.fasterxml.jackson.databind.JsonNode;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.error.Error;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.query.*;
import pl.blokaj.dbms.model.table.ColumnBase;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QueryService {
    private final Metastore metastore;
//    private final List<String> queryIDs = Collections.synchronizedList(new ArrayList<>());
    private final ConcurrentHashMap<String, Query> queries = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, QueryResult> results = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MultipleProblemsError> errors = new ConcurrentHashMap<>();
    public QueryService(Metastore m) {
        metastore = m;
    }

    public List<ShallowQuery> getQueries() {
        List<ShallowQuery> shallowQueries = new ArrayList<>();
        queries.values().forEach( query -> {
            shallowQueries.add(new ShallowQuery(query.getQueryId(), query.getStatus()));
        });
        return shallowQueries;
    }

    public Query getQueryById(String uuid) {
        return queries.getOrDefault(uuid, null);
    }

    public List<MultipleProblemsError.Problem> validateQueryPost(JsonNode queryNode) {
        return switch (QueryType.getQueryType(queryNode)) {
            case COPY -> CopyQuery.validateQuery(queryNode, metastore);
            case SELECT -> SelectQuery.validateQuery(queryNode, metastore);
            case NONE -> List.of(new MultipleProblemsError.Problem(
                    "Request body does not match with any query definition", null));
        };
    }


    public String submitQuery(QueryDefinition queryDefinition) {
        String newUuid = UUID.randomUUID().toString();
        Query newQuery = new Query(newUuid, QueryStatus.CREATED, false, queryDefinition);
        queries.put(newUuid, newQuery);
        return newUuid;
    }


    public QueryResult getQueryResult(String uuid) {
        return results.get(uuid);
    }

    public QueryResult getLimitedQueryResult(String uuid, int limit) {
        return results.get(uuid).sliceResult(limit);
    }

    public void flushQueryResult(String uuid) {
        queries.get(uuid).setResultAvailable(false);
        results.remove(uuid);
    }

    public MultipleProblemsError getQueryError(String uuid) {
        return errors.get(uuid);
    }
}
