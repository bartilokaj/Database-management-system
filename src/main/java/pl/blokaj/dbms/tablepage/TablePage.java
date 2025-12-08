package pl.blokaj.dbms.tablepage;

import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.columntype.Int64ColumnPage;
import pl.blokaj.dbms.columntype.VarcharColumnPage;
import pl.blokaj.dbms.model.table.Column;
import pl.blokaj.dbms.model.table.TableSchema;

import java.util.ArrayList;
import java.util.List;

public class TablePage {
    private final ArrayList<ColumnPage> columns;
    public TablePage() {
        columns = new ArrayList<>();
    }

    public TablePage(List<List<String>> rawStringData, TableSchema schema) throws NumberFormatException {
        columns = new ArrayList<>();
        for (int i = 0; i < schema.getColumns().size(); i++) {
            ColumnPage next = switch (schema.getColumns().get(i).getType()) {
                case INT64 -> new Int64ColumnPage(rawStringData.get(i));
                case VARCHAR -> new VarcharColumnPage(rawStringData.get(i));
            };
            columns.add(next);
        }
    }

    public void addColumn(ColumnPage column) {
        columns.add(column);
    }

    public ArrayList<ColumnPage> getColumns() {
        return columns;
    }

    public String toString() {
        ArrayList<String> list = new ArrayList<>();
        columns.forEach(columnPage -> list.add(columnPage.toString()));
        return list.toString();
    }
}
