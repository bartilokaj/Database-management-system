package pl.blokaj.dbms.columntype;

public class CompressedInt64Column extends CompressedColumn {
    private byte[] data;

    public CompressedInt64Column(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
