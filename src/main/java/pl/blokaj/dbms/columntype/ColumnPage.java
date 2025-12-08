package pl.blokaj.dbms.columntype;

import pl.blokaj.dbms.model.table.Column;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ColumnPage {
    public abstract void serialize(OutputStream stream) throws IOException;
    public abstract Object getData();
    public abstract int getRowCount();
    public abstract String calculateMetric();
    public abstract String toString();
}


