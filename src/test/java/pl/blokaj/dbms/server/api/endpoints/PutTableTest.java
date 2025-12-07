package pl.blokaj.dbms.server.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.blokaj.dbms.app.Server;
import pl.blokaj.dbms.model.table.Column;
import pl.blokaj.dbms.model.table.LogicalColumnType;
import pl.blokaj.dbms.model.table.TableSchema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutTableTest {

    @org.junit.jupiter.api.Test
    public void testPutEndpoint() throws IOException {
        try (Server testServer = new Server()) {
            Javalin app = testServer.setupServer();

            JavalinTest.test(app, (server, httpClient) -> {
                List<Column> testColumns = new ArrayList<>();
                testColumns.add(new Column("TestInt", LogicalColumnType.INT64));
                testColumns.add(new Column("TestVarchar", LogicalColumnType.VARCHAR));
                TableSchema testSchema = new TableSchema("testTable", testColumns);

                ObjectMapper mapper = new ObjectMapper();
                String jsonBody = mapper.writeValueAsString(testSchema);

                Response response = httpClient.put("/table", jsonBody);
                assertEquals(200, response.code());
            });
        }
    }

    @org.junit.jupiter.api.Test
    public void testSameNameTablePut() throws IOException {
        try (Server testServer = new Server()) {
            Javalin app = testServer.setupServer();

            JavalinTest.test(app, (server, httpClient) -> {
                List<Column> testColumns = new ArrayList<>();
                testColumns.add(new Column("TestInt", LogicalColumnType.INT64));
                testColumns.add(new Column("TestVarchar", LogicalColumnType.VARCHAR));
                TableSchema testSchema = new TableSchema("testTable", testColumns);

                ObjectMapper mapper = new ObjectMapper();
                String jsonBody = mapper.writeValueAsString(testSchema);

                Response response = httpClient.put("/table", jsonBody);
                assertEquals(200, response.code());

                Response response2 = httpClient.put("/table", jsonBody);
                assertEquals(400, response2.code());
            });
        }
    }
}
