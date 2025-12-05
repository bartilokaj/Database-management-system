package pl.blokaj.dbms.tablepage;

import pl.blokaj.dbms.columntype.ColumnPage;

import java.util.ArrayList;

public class TablePage {
    private final ArrayList<ColumnPage> columnPages;

    public TablePage() {
        columnPages = new ArrayList<>();
    }

    public void addColumn(ColumnPage columnPage) {
        columnPages.add(columnPage);
    }

    public ArrayList<ColumnPage> getColumns() {
        return columnPages;
    }
}
