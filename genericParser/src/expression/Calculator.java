package expression;

public interface Calculator<T extends Number> {
    T add(T first, T second);
    T divide(T first, T second);
    T negate(T first);
    T multiply(T first, T second);
    T subtract(T first, T second);
    T parse(String toParse);
    T min(T first, T second);
    T max(T first, T second);
    T count(T first);
}