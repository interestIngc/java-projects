package expression.parser;

public interface ExpressionSource<T extends Number> {
    boolean hasNext();
    char next();
}