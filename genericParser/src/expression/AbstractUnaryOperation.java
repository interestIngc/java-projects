package expression;

public abstract class AbstractUnaryOperation<T extends Number> implements CommonExpression<T>, UnaryFunction<T> {
    protected CommonExpression<T> expression;
    protected Calculator<T> calculator;

    public AbstractUnaryOperation(CommonExpression<T> expression, Calculator<T> calculator) {
        this.expression = expression;
        this.calculator = calculator;
    }


    @Override
    public T evaluate(T x, T y, T z) {
        return function(expression.evaluate(x, y, z));
    }
}