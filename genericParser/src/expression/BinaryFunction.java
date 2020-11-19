package expression;

public interface BinaryFunction<T extends Number> {
    T function(T first, T second);
    boolean getOrder();
}