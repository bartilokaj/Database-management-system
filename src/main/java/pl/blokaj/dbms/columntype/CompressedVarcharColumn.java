package pl.blokaj.dbms.columntype;

import java.util.ArrayList;

public class CompressedVarcharColumn extends CompressedColumn {
    private final ArrayList<CompressedVarcharPage> compressedPages;

    public CompressedVarcharColumn(ArrayList<CompressedVarcharPage> compressedPages) {
        this.compressedPages = compressedPages;
    }
}
