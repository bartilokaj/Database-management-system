package pl.blokaj.dbms.fileformat.headers;

import pl.blokaj.dbms.columntype.Column;

public class FileHeader {
    long columnNumber;

    public long getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(long columnNumber) {
        this.columnNumber = columnNumber;
    }
}
