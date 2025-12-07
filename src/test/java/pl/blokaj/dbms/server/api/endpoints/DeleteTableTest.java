package pl.blokaj.dbms.server.api.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import pl.blokaj.dbms.app.Server;
import pl.blokaj.dbms.model.table.Column;
import pl.blokaj.dbms.model.table.LogicalColumnType;
import pl.blokaj.dbms.model.table.TableSchema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteTableTest {
    @org.junit.jupiter.api.Test
    public void PutAndDelete() throws IOException {
        try (Server testServer = new Server()) {
            Javalin app = testServer.setupServer();

            JavalinTest.test(app, (server, httpClient) -> {
                List<Column> testColumns = new ArrayList<>();
                testColumns.add(new Column("TestInt", LogicalColumnType.INT64));
                testColumns.add(new Column("TestVarchar", LogicalColumnType.VARCHAR));
                TableSchema testSchema = new TableSchema("testTable", testColumns);

                ObjectMapper mapper = new ObjectMapper();
                String jsonBody = mapper.writeValueAsString(testSchema);

                Response response1 = httpClient.put("/table", jsonBody);
                assertEquals(200, response1.code());
                String tableUuid = response1.body().string();

                Response response2 = httpClient.get("/table/" + tableUuid);
                assertEquals(200, response2.code());

                Response response3 = httpClient.delete("/table/" + tableUuid);
                assertEquals(200, response3.code());

                Response response4 = httpClient.get("/table/" + tableUuid);
                assertEquals(404, response4.code());

            });

        }
    }

    @org.junit.jupiter.api.Test
    public void DeleteNonExistent() throws IOException {
        try (Server testServer = new Server()) {
            Javalin app = testServer.setupServer();

            JavalinTest.test(app, (server, httpClient) -> {

                Response response2 = httpClient.get("/table/nonexistent");
                assertEquals(404, response2.code());

                Response response3 = httpClient.delete("/table/nonexistent");
                assertEquals(404, response3.code());

            });

        }
    }
}