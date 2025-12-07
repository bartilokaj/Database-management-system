package pl.blokaj.dbms.tablepage;

import pl.blokaj.dbms.columntype.ColumnPage;

import java.util.ArrayList;

public class TablePage {
    private final ArrayList<ColumnPage> columns;

    public TablePage() {
        columns = new ArrayList<>();
    }

    public void addColumn(ColumnPage column) {
        columns.add(column);
    }

    public ArrayList<ColumnPage> getColumns() {
        return columns;
    }
}
