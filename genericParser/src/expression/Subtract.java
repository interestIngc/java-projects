package expression;

public class Subtract<T extends Number> extends AbstractBinaryOperation<T> {
    public Subtract(CommonExpression<T> firstExpression, CommonExpression<T> secondExpression, Calculator<T> calculator) {
        super(firstExpression, secondExpression, calculator);
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public boolean getOrder() {
        return true;
    }

    @Override
    public T function(T a, T b) {
        return calculator.subtract(a, b);
    }
}