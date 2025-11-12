package pl.blokaj.dbms.columntype;

import java.util.HashMap;

public class ColumnDictionary {
    private static final HashMap<Byte, Class<? extends Column>> columnDict = new HashMap<Byte, Class<? extends Column>>();

    static {
        columnDict.put((byte)0, Int64Column.class);
        columnDict.put((byte)1, VarcharColumn.class);
    }

    public static Class<? extends Column> getColumnClass(byte type) {
        return columnDict.get(type);
    }
}
