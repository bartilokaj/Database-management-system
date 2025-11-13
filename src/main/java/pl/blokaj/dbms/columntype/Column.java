package pl.blokaj.dbms.columntype;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Column {
    public abstract void serialize(OutputStream stream) throws IOException;
    public abstract Object getData();
    public abstract String calculateMetric();
}


