package expression;

public class Divide<T extends Number> extends AbstractBinaryOperation<T> {
    public Divide(CommonExpression<T> firstExpression, CommonExpression<T> secondExpression, Calculator<T> calculator) {
        super(firstExpression, secondExpression, calculator);
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public boolean getOrder() {
        return true;
    }

    @Override
    public T function(T a, T b) {
        return calculator.divide(a, b);
    }
}