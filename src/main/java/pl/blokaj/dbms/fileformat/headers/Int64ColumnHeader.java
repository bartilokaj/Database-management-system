package pl.blokaj.dbms.fileformat.headers;

/**
 * Defines header for Int64 column
 */
public class Int64ColumnHeader extends ColumnHeader {
    // number of ints in column
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
