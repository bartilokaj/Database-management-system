package pl.blokaj.dbms.model.table;

import pl.blokaj.dbms.columntype.ColumnPage;
import pl.blokaj.dbms.columntype.Int64ColumnPage;

public interface ColumnBase {
    ColumnBase sliceColumn(int limit);
    void addNextPage(ColumnPage columnPage);
}
