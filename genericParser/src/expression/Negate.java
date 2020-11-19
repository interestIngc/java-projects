package expression;

public class Negate<T extends Number> extends AbstractUnaryOperation<T> {
    public Negate(CommonExpression<T> expression, Calculator<T> calculator) {
        super(expression, calculator);
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public boolean getOrder() {
        return false;
    }

    @Override
    public T function(T a) {
        return calculator.negate(a);
    }

}