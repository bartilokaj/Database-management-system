package pl.blokaj.dbms.fileformat.headers;

import pl.blokaj.dbms.columntype.Column;

/**
 * Defines header for columns
 */
public abstract class ColumnHeader {
    // mapped to get deserializer for column
    byte columnType;
}
