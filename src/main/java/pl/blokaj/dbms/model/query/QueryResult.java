package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.blokaj.dbms.model.table.ColumnBase;
import pl.blokaj.dbms.tablepage.TablePage;

import java.util.ArrayList;
import java.util.List;

public class QueryResult {

    @JsonProperty("rowCount")
    private int rowCount;

    @JsonProperty("columns")
    private List<ColumnBase> columns;

    public QueryResult() {}

    public QueryResult(int rowCount, List<ColumnBase> columns) {
        this.rowCount = rowCount;
        this.columns = columns;
    }

    public QueryResult sliceResult(int limit) {
        if (limit > rowCount) return this;

        List<ColumnBase> newColumns = new ArrayList<>();
        columns.forEach(column ->
              newColumns.add(column.sliceColumn(limit))
                );

        return new QueryResult(limit, newColumns);
    }

    public void addNextPage(TablePage page) {
        for (int i = 0; i < columns.size(); i++ ) {
            columns.get(i).addNextPage(page.getColumns().get(i));
        }
        rowCount += page.getColumns().get(0).getRowCount();
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<ColumnBase> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnBase> columns) {
        this.columns = columns;
    }
}