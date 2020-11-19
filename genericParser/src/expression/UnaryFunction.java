package expression;

public interface UnaryFunction<T extends Number> {
    T function(T x);
}