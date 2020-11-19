package expression;

public class Multiply<T extends Number> extends AbstractBinaryOperation<T> {
    public Multiply(CommonExpression<T> firstExpression, CommonExpression<T> secondExpression, Calculator<T> calculator) {
        super(firstExpression, secondExpression, calculator);
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public boolean getOrder() {
        return false;
    }

    @Override
    public T function(T a, T b) {
        return calculator.multiply(a, b);

    }

}