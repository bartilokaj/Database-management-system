package pl.blokaj.dbms.Table;

import pl.blokaj.dbms.columntype.Column;

import java.util.ArrayList;

public class Table {
    private final ArrayList<Column> columns;

    public Table() {
        columns = new ArrayList<>();
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }
}
