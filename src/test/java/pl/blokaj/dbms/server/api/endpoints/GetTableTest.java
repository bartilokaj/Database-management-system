package pl.blokaj.dbms.server.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import pl.blokaj.dbms.app.Server;
import pl.blokaj.dbms.model.table.Column;
import pl.blokaj.dbms.model.table.LogicalColumnType;
import pl.blokaj.dbms.model.table.TableSchema;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetTableTest {

    @org.junit.jupiter.api.Test
    public void testGetTablesEndpoint() throws IOException {
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

                String tableUuid = response.body().string();
                ObjectNode expectedJson = mapper.createObjectNode();
                expectedJson.put("tableId", tableUuid);
                expectedJson.put("name", testSchema.getName());

                response = httpClient.get("/tables");
                assertEquals(List.of(expectedJson).toString(), response.body().string());

            });
        }
    }

    @org.junit.jupiter.api.Test
    public void testGetTablesEndpointMultiple() throws IOException {
        try (Server testServer = new Server()) {
            Javalin app = testServer.setupServer();

            JavalinTest.test(app, (server, httpClient) -> {
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode expectedArray = mapper.createArrayNode();

                for (int i = 1; i <= 3; i++) {
                    List<Column> testColumns = new ArrayList<>();
                    testColumns.add(new Column("TestInt" + i, LogicalColumnType.INT64));
                    testColumns.add(new Column("TestVarchar" + i, LogicalColumnType.VARCHAR));
                    TableSchema testSchema = new TableSchema("testTable" + i, testColumns);

                    String jsonBody = mapper.writeValueAsString(testSchema);

                    Response response = httpClient.put("/table", jsonBody);
                    assertEquals(200, response.code());

                    String tableUuid = response.body().string();

                    // Add to expected array
                    ObjectNode tableJson = mapper.createObjectNode();
                    tableJson.put("tableId", tableUuid);
                    tableJson.put("name", testSchema.getName());
                    expectedArray.add(tableJson);
                }

                // GET /tables
                Response getResponse = httpClient.get("/tables");
                assertEquals(200, getResponse.code());

                JsonNode actualArray = mapper.readTree(getResponse.body().string());

                Set<String> expectedSet = new HashSet<>();
                expectedArray.forEach(node -> expectedSet.add(node.toString()));

                Set<String> actualSet = new HashSet<>();
                actualArray.forEach(node -> actualSet.add(node.toString()));

                assertEquals(expectedSet, actualSet);
            });
        }
    }
}
