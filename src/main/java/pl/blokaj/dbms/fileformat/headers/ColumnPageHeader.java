package pl.blokaj.dbms.fileformat.headers;

/**
 * Defines header for columns
 */
public abstract class ColumnPageHeader {
    // mapped to get deserializer for column
    byte columnType;
}
