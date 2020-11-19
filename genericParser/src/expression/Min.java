package expression;

public class Min<T extends Number> extends AbstractBinaryOperation<T> {
    public Min(CommonExpression<T> a, CommonExpression<T> b, Calculator<T> calculator) {
        super(a, b, calculator);
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public T function(T a, T b) {
        return calculator.min(a, b);
    }

    @Override
    public boolean getOrder() {
        return true;
    }
}