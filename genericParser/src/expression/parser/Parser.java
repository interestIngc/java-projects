package expression.parser;

import expression.GenericTripleExpression;

public interface Parser<T extends Number> {
    GenericTripleExpression parse(String expression);
}