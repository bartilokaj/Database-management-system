package pl.blokaj.dbms.model.query;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import jakarta.annotation.Nonnull;
import pl.blokaj.dbms.metastore.Metastore;
import pl.blokaj.dbms.model.error.MultipleProblemsError;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Description of the COPY query from CSV file.
 * Server will read the file and insert all data into selected table.
 * When number of columns in source and target doesn't match,
 * user has to use "destinationColumns" property to specify which columns data should be inserted into.
 */
public class CopyQuery implements QueryDefinition {

    /**
     * Path to source CSV file (filepath in perspective of running server! NOT client)
     */
    @JsonProperty("sourceFilepath")
    @Nonnull
    private String sourceFilepath;

    /**
     * Name of the destination table to copy data into
     */
    @JsonProperty("destinationTableName")
    @Nonnull
    private String destinationTableName;

    /**
     * List of columns to copy data into.
     * It creates a map from source columns to destination columns.
     * Assumes that data in source file is in the same order as in this list.
     */
    @JsonProperty("destinationColumns")
    private List<String> destinationColumns;

    /**
     * Whether CSV file contains header row
     * Default is false
     */
    @JsonProperty("doesCsvContainHeader")
    private Boolean doesCsvContainHeader = false;

    /**
     * Default constructor
     */
    public CopyQuery() {
    }

    /**
     * Constructor with all fields
     */
    public CopyQuery(@Nonnull String sourceFilepath,
                     @Nonnull String destinationTableName,
                     List<String> destinationColumns,
                     Boolean doesCsvContainHeader) {
        this.sourceFilepath = sourceFilepath;
        this.destinationTableName = destinationTableName;
        this.destinationColumns = destinationColumns;
        this.doesCsvContainHeader = doesCsvContainHeader != null ? doesCsvContainHeader : false;
    }

    public static List<MultipleProblemsError.Problem> validateQuery(JsonNode copyNode, Metastore m) {
        List<MultipleProblemsError.Problem> problems = new ArrayList<>();

        String csvPath = copyNode.get("sourceFilepath").asText();
        Path path = Path.of(csvPath);
        if (!Files.exists(path)) problems.add(new MultipleProblemsError.Problem("Specified file does not exist", csvPath));

        String tableName = copyNode.get("destinationTableName").asText();
        if (m.getTableUuid(tableName) == null) problems.add(new MultipleProblemsError.Problem("Table of that name does not exist", tableName));
        else {
            Set<String> columnNames = m.getTableColumnNames(tableName);
            JsonNode destinationColumnsNode = copyNode.get("destinationColumns");
            if (destinationColumnsNode != null) {
                if (!destinationColumnsNode.isArray()) problems.add(new MultipleProblemsError.Problem("destinationColumns is not an array", null));
                else {
                    for (JsonNode col : destinationColumnsNode) {
                        if (!columnNames.contains(col.asText())) {
                            problems.add(new MultipleProblemsError.Problem("Column does not exist in destination table", col.asText()));
                        }
                    }
                }
            }
        }

        JsonNode headerFlagNode = copyNode.get("doesCsvContainHeader");
        if (headerFlagNode != null && !headerFlagNode.isBoolean()) {
            problems.add(new MultipleProblemsError.Problem("doesCsvContainHeader is not boolean", null));
        }

        return problems;
    }

    // Getters and setters

    public String getSourceFilepath() {
        return sourceFilepath;
    }

    public void setSourceFilepath(@Nonnull String sourceFilepath) {
        this.sourceFilepath = sourceFilepath;
    }

    public String getDestinationTableName() {
        return destinationTableName;
    }

    public void setDestinationTableName(@Nonnull String destinationTableName) {
        this.destinationTableName = destinationTableName;
    }

    public List<String> getDestinationColumns() {
        return destinationColumns;
    }

    public void setDestinationColumns(List<String> destinationColumns) {
        this.destinationColumns = destinationColumns;
    }

    public Boolean getDoesCsvContainHeader() {
        return doesCsvContainHeader;
    }

    public void setDoesCsvContainHeader(Boolean doesCsvContainHeader) {
        this.doesCsvContainHeader = doesCsvContainHeader != null ? doesCsvContainHeader : false;
    }
}
