package expression;

public interface GenericTripleExpression<T extends Number> {
    T evaluate(T x, T y, T z);
}