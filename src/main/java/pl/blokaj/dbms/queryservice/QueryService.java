package pl.blokaj.dbms.queryservice;

import com.fasterxml.jackson.databind.JsonNode;
import pl.blokaj.dbms.filesystem.TableFilesManager;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.query.*;
import pl.blokaj.dbms.querytasks.CopyQueryTask;
import pl.blokaj.dbms.querytasks.SelectQueryTask;

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

        Thread queryThread = null;
        String tableUuid = metastore.getTableUuid(queryDefinition.getTableName());
        TableFilesManager manager = metastore.getTableFileManager(tableUuid);
        switch (queryDefinition.getQueryType()) {
            case COPY -> queryThread = new Thread(new CopyQueryTask(newQuery, this, manager));
            case SELECT -> queryThread = new Thread(new SelectQueryTask(newQuery, this, manager));
        }
        if (queryThread != null) queryThread.start();

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

    public void submitQueryError(MultipleProblemsError error, Query query) {
        String uuid = query.getQueryId();
        query.setStatus(QueryStatus.FAILED);
        errors.put(uuid, error);
    }

    public void submitQueryResult(QueryResult result, Query query) {
        String uuid = query.getQueryId();
        query.setStatus(QueryStatus.COMPLETED);
        query.setResultAvailable(true);
        results.put(uuid, result);
    }
}
