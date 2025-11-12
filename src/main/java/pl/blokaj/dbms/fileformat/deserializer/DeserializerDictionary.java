package pl.blokaj.dbms.fileformat.deserializer;

import java.util.HashMap;
import java.util.Map;

public class DeserializerDictionary {
    private static final Map<Byte, ColumnDeserializer<?>> deserializerDictionary  = new HashMap<>();

    static {
        deserializerDictionary.put((byte)0, Int64Deserializer.INSTANCE);
        deserializerDictionary.put((byte)1, VarcharDeserializer.INSTANCE);
    }

    public static ColumnDeserializer<?> getColumnDeserializer(byte type) {
        return deserializerDictionary.get(type);
    }
}
