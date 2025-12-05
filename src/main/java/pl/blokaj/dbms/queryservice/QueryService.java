package pl.blokaj.dbms.queryservice;

import pl.blokaj.dbms.metastore.Metastore;

import java.util.concurrent.ConcurrentHashMap;

public class QueryService {
    private final Metastore metastore;
    public QueryService(Metastore m) {
        metastore = m;
    }
}
