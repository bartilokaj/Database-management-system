package pl.blokaj.dbms.fileformat.encoding;

import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorSpecies;

/**
 * Handles delta encoding using SIMD operations
 */
public class Delta {
    private static final VectorSpecies<Long> SPECIES = LongVector.SPECIES_PREFERRED;

    public static long[] encodeDelta(long[] data) {
        long[] delta = new long[data.length];
        delta[0] = data[0];
        int i;
        // SIMD data
        for (i = 1; i + SPECIES.length() <= data.length; i += SPECIES.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES, data, i);
            LongVector v2 = LongVector.fromArray(SPECIES, data, i - 1);
            LongVector diff = v1.sub(v2);
            diff.intoArray(delta, i);
        }
        // Process tail
        for ( ; i < data.length; i++) {
            delta[i] = data[i] - data[i - 1];
        }
        return delta;
    }

    public static long[] decodeDelta(long[] data) {
        for (int i = 1; i < data.length; i++) {
            data[i] += data[i - 1];
        }
        return data;
    }
}
