package pl.blokaj.dbms.fileformat.headers;

/**
 * Defines varchar column header
 */
public class VarcharColumnHeader extends ColumnHeader {
    // number of entries
    private long columnSize;
    // number of bytes that are in the column
    private long dataSize;

    public long getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(long columnSize) {
        this.columnSize = columnSize;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public VarcharColumnHeader() {}

}
