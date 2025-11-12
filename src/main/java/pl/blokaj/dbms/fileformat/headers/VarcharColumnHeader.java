package pl.blokaj.dbms.fileformat.headers;

public class VarcharColumnHeader extends ColumnHeader {
    private long columnSize;
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
