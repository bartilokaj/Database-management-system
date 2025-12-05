package pl.blokaj.dbms.columntype;

import java.util.HashMap;

/**
 * Maps bytes to Column classes, and vice versa
 */
public class ColumnDictionary {
    private static final HashMap<Byte, Class<? extends ColumnPage>> columnDict = new HashMap<Byte, Class<? extends ColumnPage>>();
    private static final HashMap<Class<? extends ColumnPage>, Byte> typeDict = new HashMap<>();

    static {
        columnDict.put((byte)0, Int64ColumnPage.class);
        columnDict.put((byte)1, VarcharColumnPage.class);
    }

    static {
        typeDict.put((Int64ColumnPage.class), (byte)0);
        typeDict.put((VarcharColumnPage.class), (byte)1);
    }

    public static Class<? extends ColumnPage> getColumnClass(byte type) {
        return columnDict.get(type);
    }

    public static Byte getColumnType(Class<? extends ColumnPage> columnClass) {
        return typeDict.get(columnClass);
    }
}
