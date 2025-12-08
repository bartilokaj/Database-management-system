package pl.blokaj.dbms.server.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

public class CopyAndSelectTest {

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


                String filePath = "src/main/resources/small.csv";

                CopyQuery copyQuery = new CopyQuery(filePath, "testTable", null, null);
                String copyQueryBody = mapper.writeValueAsString(copyQuery);
                Response response1 = httpClient.post("/query", copyQueryBody);
                assertEquals(200, response1.code());
                String copyQueryId = response1.body().string();

                while (true) {
                    Response response2 = httpClient.get("/query/" + copyQueryId);
                    String resultString = response2.body().string();
                    JsonNode node = mapper.readTree(resultString);
                    if (Objects.equals(node.get("status").asText(), "COMPLETED")) {
                        break;
                    }
                    sleep(2000);
                }

                SelectQuery selectQuery = new SelectQuery();
                selectQuery.setTableName("testTable");
                String selectQueryBody = mapper.writeValueAsString(selectQuery);
                Response response3 = httpClient.post("/query", selectQueryBody);
                assertEquals(200, response1.code());
                String selectQueryId = response3.body().string();

                while (true) {
                    Response response4 = httpClient.get("/query/" + selectQueryId);
                    String resultString = response4.body().string();
                    JsonNode node = mapper.readTree(resultString);
                    if (Objects.equals(node.get("status").asText(), "COMPLETED")) break;
                    sleep(2000);
                }

                Response response5 = httpClient.get("/result/" + selectQueryId);
                assertEquals(200, response.code());
            });
        }
    }
}
