package expression;

public abstract class AbstractBinaryOperation<T extends Number> implements CommonExpression<T>, BinaryFunction<T> {
    protected CommonExpression<T> firstExpression;
    protected CommonExpression<T> secondExpression;
    protected Calculator<T> calculator;

    protected AbstractBinaryOperation(CommonExpression<T> firstExpression, CommonExpression<T> secondExpression, Calculator<T> calculator) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return function(firstExpression.evaluate(x, y, z), secondExpression.evaluate(x, y, z));
    }


}