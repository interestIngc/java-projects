package expression;


public class Count<T extends Number> extends AbstractUnaryOperation<T> {
    public Count(CommonExpression<T> expression, Calculator<T> calculator) {
        super(expression, calculator);
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public boolean getOrder() {
        return true;
    }

    @Override
    public T function(T i) {
        return calculator.count(i);
    }

}