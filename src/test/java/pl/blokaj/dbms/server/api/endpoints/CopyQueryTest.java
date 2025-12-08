package pl.blokaj.dbms.server.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import pl.blokaj.dbms.app.Server;
import pl.blokaj.dbms.model.query.CopyQuery;
import pl.blokaj.dbms.model.query.SelectQuery;
import pl.blokaj.dbms.model.table.Column;
import pl.blokaj.dbms.model.table.LogicalColumnType;
import pl.blokaj.dbms.model.table.TableSchema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CopyQueryTest {

    @org.junit.jupiter.api.Test
    public void CopyTest() throws IOException {
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

                Response r = httpClient.get("/table/" + tableUuid);
                assertEquals(200, r.code());


                String filePath = "src/main/resources/onlyInt.csv";

                CopyQuery query = new CopyQuery(filePath, "testTable", null, null);
                String queryBody = mapper.writeValueAsString(query);
                Response response1 = httpClient.post("/query", queryBody);
                assertEquals(200, response1.code());
                String queryId = response1.body().string();

                while (true) {
                    Response response2 = httpClient.get("/query/" + queryId);
                    String resultString = response2.body().string();
                    JsonNode node = mapper.readTree(resultString);
                    if (Objects.equals(node.get("status").asText(), "COMPLETED")) {
                        assertEquals("", resultString);
                        break;
                    }
                    sleep(2000);
                }
            });
        }
    }
}
