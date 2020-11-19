package expression;

public class Const<T extends Number> implements CommonExpression<T> {
    private T number;
    protected Calculator<T> calculator;
    public Const (T number, Calculator<T> calculator) {
        this.number = number;
        this.calculator = calculator;
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
    public T evaluate(T x, T y, T z) {
        return number;
    }
}