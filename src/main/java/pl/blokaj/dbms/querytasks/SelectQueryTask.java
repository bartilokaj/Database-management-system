package pl.blokaj.dbms.querytasks;

import pl.blokaj.dbms.fileformat.deserializer.FileDeserializer;
import pl.blokaj.dbms.filesystem.TableFilesManager;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.query.Query;
import pl.blokaj.dbms.model.query.QueryResult;
import pl.blokaj.dbms.model.query.QueryStatus;
import pl.blokaj.dbms.model.table.ColumnBase;
import pl.blokaj.dbms.model.table.Int64Column;
import pl.blokaj.dbms.model.table.VarcharColumn;
import pl.blokaj.dbms.queryservice.QueryService;
import pl.blokaj.dbms.tablepage.TablePage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SelectQueryTask implements Runnable {
    private final TableFilesManager manager;
    private final QueryService service;
    private final Query query;

    public SelectQueryTask(Query query, QueryService service, TableFilesManager manager) {
        this.manager = manager;
        this.service = service;
        this.query = query;
    }

    @Override
    public void run() {
        query.setStatus(QueryStatus.PLANNING);
        QueryResult result = new QueryResult();
        result.setRowCount(0);
        List<ColumnBase> queryColumns = new ArrayList<>();
        manager.getTableSchema().getColumns().forEach(column -> {
            ColumnBase nextColumn = switch (column.getType()) {
                case INT64 -> new Int64Column(new ArrayList<>());
                case VARCHAR -> new VarcharColumn(new ArrayList<>());
            };
            queryColumns.add(nextColumn);
        });
        result.setColumns(queryColumns);


        manager.lockReadLock();

        query.setStatus(QueryStatus.RUNNING);
        Path dataDir = Path.of("data");
        for (String fileName: manager.getFileNames()) {
            Path filePath = dataDir.resolve(fileName);
            try (InputStream in = Files.newInputStream(filePath)) {
                TablePage nextPage = FileDeserializer.deserializeFile(in);
                result.addNextPage(nextPage);
            } catch (IOException e) {
                MultipleProblemsError problems = new MultipleProblemsError(
                        List.of(new MultipleProblemsError.Problem(
                                "IOException while reading file " + e.getMessage(),
                                fileName)));
                service.submitQueryError(problems, query);
                manager.unlockReadLock();
                return;
            }
        }
        service.submitQueryResult(result, query);
        manager.unlockReadLock();
    }
}
