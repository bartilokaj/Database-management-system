package pl.blokaj.dbms.fileformat.encoding;

import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

/**
 * Handles ZigZag encoding using SIMD operations
 */
public class ZigZag {
    private static final VectorSpecies<Long> SPECIES = LongVector.SPECIES_PREFERRED;

    // (x << 1) ^ (x >> 63)
    public static long[] encodeZigZag(long[] data) {
        int i;
        for (i = 0; i + SPECIES.length() <= data.length; i += SPECIES.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES, data, i);
            LongVector shifted = v1.lanewise(VectorOperators.LSHL, 1);
            LongVector sign = v1.lanewise(VectorOperators.ASHR, 63);
            LongVector zigZag = shifted.lanewise(VectorOperators.XOR, sign);
            zigZag.intoArray(data, i);
        }
        for ( ; i < data.length; i++ ) {
            data[i] = (data[i] << 1) ^ (data[i] >> 63);
        }
        return data;
    }

    // (x >>> 1) ^ -(x & 1)
    public static long[] decodeZigZag(long[] data) {
        int i;
        for ( i = 0; i + SPECIES.length() <= data.length; i += SPECIES.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES, data, i);
            LongVector shifted = v1.lanewise(VectorOperators.LSHR, 1);
            LongVector signMask = v1.and(LongVector.broadcast(SPECIES, 1L));
            LongVector sign = signMask.neg();

            LongVector decoded = shifted.lanewise(VectorOperators.XOR, sign);
            decoded.intoArray(data, i);
        }
        for ( ; i < data.length; i++ ) {
            data[i] = (data[i] >>> 1) ^ -(data[i] & 1);
        }

        return data;
    }
}
