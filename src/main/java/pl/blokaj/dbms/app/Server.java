package pl.blokaj.dbms.app;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;
import io.javalin.openapi.plugin.OpenApiPlugin;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.error.Error;
import pl.blokaj.dbms.model.query.*;
import pl.blokaj.dbms.model.table.ShallowTable;
import pl.blokaj.dbms.model.table.TableSchema;
import pl.blokaj.dbms.queryservice.QueryService;
import pl.blokaj.dbms.queryservice.QueryType;

import java.io.IOException;
import java.util.List;

public class Server implements AutoCloseable {
    private static Metastore metastore;
    private static QueryService queryService;

    Server() throws IOException {
        metastore = new Metastore();
        queryService = new QueryService(metastore);
    }

    public void runServer() throws IOException {
        OpenApiPlugin openApiPlugin = new OpenApiPlugin(cfg -> {
            cfg.withDefinitionConfiguration((doc, defCfg) ->
                    defCfg.withInfo(info -> {
                        info.setTitle("Database API");
                        info.setVersion("1.0");
                    })
            );
        });

        var app = Javalin.create(javalinConfig -> {
            javalinConfig.registerPlugin(openApiPlugin);
        });

        app.get("/tables", this::getTablesHandler);
        app.get("/table/{tableId}", this::getTableByIdHandler);
        app.delete("/table/{tableId}", this::deleteTableHandler);
        app.put("/table", this::createTableHandler);
        app.get("/queries", this::getQueriesHandler);
        app.get("/query/{queryId}", this::getQueryByIdHandler);
        app.post("/query", this::submitQueryHandler);
        app.get("/result/{queryId}", this::getQueryResultHandler);
        app.get("/error/{queryId}", this::getQueryErrorHandler);
        app.get("/system/info", this::getSystemInfo);

    }

    @Override
    public void close() throws IOException {
        metastore.close();
    }


    @OpenApi(
            summary = "Get list of tables with their accompanying IDs...",
            operationId = "getTables",
            tags = {"schema", "proj3"},
            path = "/tables",
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            content = {@OpenApiContent(from = ShallowTable[].class)}
                    )
            }
    )
    private void getTablesHandler(Context context) {
        List<ShallowTable> tables = metastore.getShallowTables();
        context.status(200).json(tables);
    }


    private void getTableByIdHandler(Context context) {
        String uuid = context.pathParam("tableId");
        TableSchema requestSchema = metastore.getTableById(uuid);
        if (requestSchema != null) {
            context.status(200).json(requestSchema);
        }
        else {
            context.status(404).json(new Error("Couldn't find a table of given ID"));
        }
    }

    private void deleteTableHandler(Context context) {
        String tableId = context.pathParam("tableId");
        boolean result = metastore.deleteTable(tableId);
        if (result) {
            context.status(200);
        }
        else {
            context.status(404).json(new Error("Couldn't find a table of given ID"));
        }
    }

    private void createTableHandler(Context context) {
        List<MultipleProblemsError.Problem> problems = metastore.validateTablePut(context.bodyAsClass(JsonNode.class));
        if (problems.isEmpty()) {
            String newUuid = metastore.createTable(context.bodyAsClass(TableSchema.class));
            context.status(200).json(newUuid);
        }
        else {
            context.status(400).json(problems);
        }
    }

    private void getQueriesHandler(Context context) {
        context.json(queryService.getQueries());
    }

    private void getQueryByIdHandler(Context context) {
        String queryId = context.pathParam("queryId");
        Query result = queryService.getQueryById(queryId);
        if (result == null) {
            context.status(404).json(new Error("Couldn't find a query of given ID"));
        }
        else {
            context.status(200).json(result);
        }
    }

    private void submitQueryHandler(Context context) {
        JsonNode queryNode = context.bodyAsClass(JsonNode.class);
        List<MultipleProblemsError.Problem> problems = queryService.validateQueryPost(queryNode);
        if (problems.isEmpty()) {
            QueryDefinition queryDefinition = switch (QueryType.getQueryType(queryNode)) {
                case COPY -> context.bodyAsClass(CopyQuery.class);
                case SELECT -> context.bodyAsClass(SelectQuery.class);
                case NONE -> null;
            };
            if (queryDefinition == null) context.status(400);
            else {
                String queryUuid = queryService.submitQuery(queryDefinition);
                context.status(200).json(queryUuid);
            }
        }
        else {
            context.status(400).json(problems);
        }
    }

    private void getQueryResultHandler(Context context) {
        String uuid = context.pathParam("queryId");
        Query query = queryService.getQueryById(uuid);
        if (query == null) context.status(404).json(new Error("Couldn't find a query of given ID"));
        else if (!query.isResultAvailable()) context.status(400).json(new Error("Result of this query is not available"));
        else if (context.body().isEmpty()) {
            QueryResult result = queryService.getQueryResult(uuid);
            context.status(200).json(result);
        }
        else {
            JsonNode requestNode = context.bodyAsClass(JsonNode.class);
            JsonNode rowLimitNode = requestNode.get("rowLimit");
            JsonNode flushResult = requestNode.get("flushResult");

            if (rowLimitNode != null && !rowLimitNode.isInt()) {
                context.status(200).json(queryService.getLimitedQueryResult(uuid, rowLimitNode.asInt()));
            }
            else {
                context.status(200).json(queryService.getQueryResult(uuid));
            }

            if (flushResult != null && !flushResult.isBoolean()) {
                queryService.flushQueryResult(uuid);
            }
        }
    }
    
    private void getQueryErrorHandler(Context context) {
        String uuid = context.pathParam("queryId");
        Query query = queryService.getQueryById(uuid);
        if (query == null) context.status(404).json(new Error("Couldn't find a query of given ID"));
        else if (!query.isResultAvailable()) context.status(400).json(new Error("Result of this query is not available"));
        else {
            MultipleProblemsError error = queryService.getQueryError(uuid);
            context.status(200).json(error);
        }
    }

    private void getSystemInfo(Context context) {
        // TODO
    }
}
