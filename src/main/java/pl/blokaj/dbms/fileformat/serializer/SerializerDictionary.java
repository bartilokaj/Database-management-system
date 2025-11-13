package pl.blokaj.dbms.fileformat.serializer;

import pl.blokaj.dbms.fileformat.deserializer.ColumnDeserializer;
import pl.blokaj.dbms.fileformat.deserializer.Int64Deserializer;
import pl.blokaj.dbms.fileformat.deserializer.VarcharDeserializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerDictionary {
    private static final Map<Byte, ColumnSerializer<?>> serializerDictionary  = new HashMap<>();

    static {
        serializerDictionary.put((byte)0, Int64Serializer.INSTANCE);
        serializerDictionary.put((byte)1, VarcharSerializer.INSTANCE);
    }

    public static ColumnSerializer<?> getColumnSerializer(byte type) {
        return serializerDictionary.get(type);
    }
}
