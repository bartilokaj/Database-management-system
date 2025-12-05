package pl.blokaj.dbms.app;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import io.javalin.openapi.plugin.OpenApiPlugin;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.table.ShallowTable;
import pl.blokaj.dbms.model.table.TableSchema;

import java.io.IOException;
import java.util.List;

public class Server implements AutoCloseable {
    private static Metastore metastore;

    Server() throws IOException {
        metastore = new Metastore();
    }

    public void runServer() throws IOException {
        OpenApiPlugin openApiPlugin = new OpenApiPlugin(cfg -> {
            cfg.withDefinitionConfiguration((doc, defCfg) ->
                    defCfg.withInfo(info -> {
                        info.setTitle("Database API");
                        info.setVersion("1.0");
                    });
            );
        });

        var app = Javalin.create(javalinConfig -> {
            javalinConfig.registerPlugin(openApiPlugin);
        });

        app.get("/tables", this::getTablesHandler);
        app.get("/table/{tableId}", this::getTableByIdHandler);
        app.delete("/table/{tableId}", );
        app.put("/table",);
        app.get("/queries",);
        app.get("/query/{queryId}",);
        app.port("/query",);
        app.get("/result/{queryId}");
        app.get("/error/{queryId}");
        app.get("/system/info");

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
    private void getTablesHandler(Context ctx) {
        List<ShallowTable> tables = metastore.getShallowTables();
        ctx.status(200);
        ctx.json(tables);
    }


    public void getTableByIdHandler(Context ctx) {
        String uuid = ctx.pathParam("tableId");
        TableSchema requestSchema = metastore.getTableById(uuid);
        if (requestSchema != null) {
            ctx.status(200);
            ctx.json(requestSchema);
        }
        else {
            ctx.status(404);
            ctx.json(new Error("Couldn't find a table of given ID"));
        }
    }
}
