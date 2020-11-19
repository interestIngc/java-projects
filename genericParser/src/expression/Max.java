package expression;

public class Max<T extends Number> extends AbstractBinaryOperation<T> {
    public Max(CommonExpression<T> firstExpression, CommonExpression<T> secondExpression, Calculator<T> calculator) {
        super(firstExpression, secondExpression, calculator);
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public boolean getOrder() {
        return true;
    }

    @Override
    public T function(T first, T second) {
        return calculator.max(first, second);
    }
}