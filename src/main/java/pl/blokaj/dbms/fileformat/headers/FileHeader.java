package pl.blokaj.dbms.fileformat.headers;

import pl.blokaj.dbms.columntype.Column;

/**
 * Defines file header
 */
public class FileHeader {
    // number of columns
    long columnNumber;

    public long getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(long columnNumber) {
        this.columnNumber = columnNumber;
    }
}
