package pl.blokaj.dbms.fileformat.headers;

/**
 * Defines header for Int64 column
 */
public class Int64ColumnPageHeader extends ColumnPageHeader {
    // number of ints in column
    private long dataLength;

    public Int64ColumnPageHeader() {
    }

    public void setDataLength(long dataLength) {
        this.dataLength = dataLength;
    }

    public long getDataLength() {
        return dataLength;
    }
}
