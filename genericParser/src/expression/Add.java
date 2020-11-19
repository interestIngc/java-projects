package expression;

public class Add<T extends Number> extends AbstractBinaryOperation<T> {
    public Add(CommonExpression<T> firstExpression, CommonExpression<T> secondExpression, Calculator<T> calculator) {
        super(firstExpression, secondExpression,  calculator);
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public boolean getOrder() {
        return false;
    }

    @Override
    public T function(T first, T second) {
        return calculator.add(first, second);
    }


}