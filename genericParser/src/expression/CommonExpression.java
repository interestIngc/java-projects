package expression;

public interface CommonExpression<T extends Number> extends GenericTripleExpression<T> {
    int getLevel();
    boolean getOrder();
}