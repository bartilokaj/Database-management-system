package pl.blokaj.dbms.querytasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.blokaj.dbms.fileformat.serializer.FileSerializer;
import pl.blokaj.dbms.filesystem.TableFilesManager;
import pl.blokaj.dbms.model.error.MultipleProblemsError;
import pl.blokaj.dbms.model.query.CopyQuery;
import pl.blokaj.dbms.model.query.Query;
import pl.blokaj.dbms.model.query.QueryStatus;
import pl.blokaj.dbms.model.table.TableSchema;
import pl.blokaj.dbms.queryservice.QueryService;
import pl.blokaj.dbms.tablepage.TablePage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CopyQueryTask implements Runnable {
    private final Query query;
    private final QueryService service;
    private final TableFilesManager manager;

    public CopyQueryTask(Query query, QueryService service, TableFilesManager manager) {
        this.query = query;
        this.service = service;
        this.manager = manager;
    }

    @Override
    public void run() {
        query.setStatus(QueryStatus.PLANNING);
        CopyQuery queryDefinition = (CopyQuery) query.getQueryDefinition();
        TableSchema schema = manager.getTableSchema();
        Path path = Path.of(queryDefinition.getSourceFilepath());
        List<MultipleProblemsError.Problem> problems = new ArrayList<>();
        List<String> addedFiles = new ArrayList<>();
        try (
            InputStream in = Files.newInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
        ) {
            int[] columnMapping;

            if (queryDefinition.getDoesCsvContainHeader() && queryDefinition.getDestinationColumns() == null) {
                String headerRow = reader.readLine();
                String[] columnNames = headerRow.split(",");
                HashSet<String> fileColumns = new HashSet<>(Arrays.asList(columnNames));
                schema.getColumns().forEach(column -> {
                    if (!fileColumns.contains(column.getName())) {
                        problems.add(new MultipleProblemsError.Problem("Table column missing in file", column.getName()));
                    }
                });

                columnMapping = new int[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    columnMapping[i] = schema.getColumnIndex(columnNames[i]);
                }
            }
            else if (queryDefinition.getDestinationColumns() != null) {
                HashSet<String> destinationColumns = new HashSet<>(queryDefinition.getDestinationColumns());
                schema.getColumns().forEach(column -> {
                    if (!destinationColumns.contains(column.getName())) {
                        problems.add(new MultipleProblemsError.Problem("Table column missing in column map", column.getName()));
                    }
                });

                columnMapping = new int[schema.getColumns().size()];
                List<String> queryDestinationColumns = queryDefinition.getDestinationColumns();
                for (int i = 0; i < columnMapping.length; i++) {
                    columnMapping[i] = schema.getColumnIndex(queryDestinationColumns.get(i));
                }
            }
            // assume that columns are exact match
            else {
                columnMapping = new int[schema.getColumns().size()];
                for (int i = 0; i < columnMapping.length; i++) {
                    columnMapping[i] = i;
                }
            }

            if (!problems.isEmpty()) {
                MultipleProblemsError error = new MultipleProblemsError(problems);
                service.submitQueryError(error, query);
                return;
            }

            query.setStatus(QueryStatus.RUNNING);

            String line;
            List<List<String>> rawDataStrings = new ArrayList<>(schema.getColumns().size());
            for (int i = 0; i < schema.getColumns().size(); i++) {
                rawDataStrings.add(new ArrayList<>(8192));
            }

            int batchCount = 0;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(",");
                for (int i = 0; i < columnMapping.length; i++) {
                    if (columnMapping[i] >= 0) {
                        rawDataStrings.get(columnMapping[i]).add(lineSplit[i]);
                    }
                }
                batchCount++;
                if (batchCount == 8192) {
                    TablePage next = new TablePage(rawDataStrings, schema);
                    String nextUuid = UUID.randomUUID().toString();
                    Path dataDir = Path.of("data");

                    Path test = dataDir.resolve("test.txt");
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(test.toFile(), next);


                    Path filePath = dataDir.resolve(nextUuid);
                    FileSerializer.toFile(filePath.toAbsolutePath().toString(), next);
                    addedFiles.add(nextUuid);
                    rawDataStrings.forEach(List::clear);
                    batchCount = 0;
                }
            }
            if (batchCount > 0) {
                for (int i = 0; i < rawDataStrings.size(); i++) {
                    rawDataStrings.set(i, new ArrayList<>(rawDataStrings.get(i).subList(0, batchCount)));
                }
                TablePage next = new TablePage(rawDataStrings, schema);
                String nextUuid = UUID.randomUUID().toString();
                Path dataDir = Path.of("data");
                Path filePath = dataDir.resolve(nextUuid);
                FileSerializer.toFile(filePath.toAbsolutePath().toString(), next);
                addedFiles.add(nextUuid);
            }

            manager.lockWriteLock();
            manager.getFileNames().addAll(addedFiles);
            query.setResultAvailable(true);
            query.setStatus(QueryStatus.COMPLETED);
            manager.unlockWriteLock();

        } catch (IOException e) {
            problems.add(new MultipleProblemsError.Problem(
                    "IOException while reading file " + e.getMessage(),
                    queryDefinition.getSourceFilepath()));
            MultipleProblemsError error = new MultipleProblemsError(problems);
            service.submitQueryError(error, query);
            return;
        } catch (NumberFormatException e) {
            problems.add(new MultipleProblemsError.Problem(
                    "NumberFormatException while reading int column" + e.getMessage(),
                    queryDefinition.getSourceFilepath()));
            MultipleProblemsError error = new MultipleProblemsError(problems);
            service.submitQueryError(error, query);
            return;
        }
    }
}
