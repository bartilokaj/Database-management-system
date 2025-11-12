package pl.blokaj.dbms.fileformat.headers;

import pl.blokaj.dbms.columntype.Column;

public class FileHeader {
    long fileSize;
    long columnNumber;
    Class<Column>[] columnTypes;
}
