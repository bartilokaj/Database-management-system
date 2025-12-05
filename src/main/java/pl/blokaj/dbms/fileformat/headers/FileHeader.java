package pl.blokaj.dbms.fileformat.headers;

/**
 * Defines file header
 */
public class FileHeader {
    // number of columns
    long columnSize;

    public long getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(long columnNumber) {
        this.columnSize = columnNumber;
    }
}
