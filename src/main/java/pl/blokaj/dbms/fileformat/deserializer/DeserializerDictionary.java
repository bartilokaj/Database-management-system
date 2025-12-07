package pl.blokaj.dbms.fileformat.deserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps bytes to deserializer instances
 */
public class DeserializerDictionary {
    private static final Map<Byte, ColumnPageDeserializer<?>> deserializerDictionary  = new HashMap<>();

    static {
        deserializerDictionary.put((byte)0, Int64PageDeserializer.INSTANCE);
        deserializerDictionary.put((byte)1, VarcharPageDeserializer.INSTANCE);
    }

    public static ColumnPageDeserializer<?> getColumnDeserializer(byte type) {
        return deserializerDictionary.get(type);
    }
}
