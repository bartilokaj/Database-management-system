package pl.blokaj.dbms.utility;

@FunctionalInterface
public interface TriFunction<A,B,C,R> {
    R apply(A a, B b, C c);
}
