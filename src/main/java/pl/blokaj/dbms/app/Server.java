package pl.blokaj.dbms.app;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import io.javalin.openapi.plugin.OpenApiPlugin;
import org.jetbrains.annotations.NotNull;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.table.ShallowTable;
import pl.blokaj.dbms.model.table.TableSchema;
import pl.blokaj.dbms.queryservice.QueryService;

import java.io.IOException;
import java.util.Collections;
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
        app.put("/table", this::createTable);
        app.get("/queries",);
        app.get("/query/{queryId}",);
        app.post("/query",);
        app.get("/result/{queryId}",);
        app.get("/error/{queryId}",);
        app.get("/system/info",);

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

    private void createTable(Context context) {
        List<MultipleProblemsError.Problem> problems = metastore.validateTablePut(context.bodyAsClass(JsonNode.class));
        if (problems.isEmpty()) {
            String newUUID = metastore.createTable(context.bodyAsClass(TableSchema.class));
            context.status(200).json(newUUID);
        }
        else {
            context.status(400).json(problems);
        }
    }

    private void
}
