package pl.blokaj.dbms.model.query;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import java.util.List;

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
