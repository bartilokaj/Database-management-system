package pl.blokaj.dbms.fileformat.headers;

public class Int64ColumnHeader extends ColumnHeader {
    private long dataLength;

    public Int64ColumnHeader() {
    }

    public void setDataLength(long dataLength) {
        this.dataLength = dataLength;
    }

    public long getDataLength() {
        return dataLength;
    }
}
