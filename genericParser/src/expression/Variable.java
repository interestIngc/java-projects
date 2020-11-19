package expression;

import java.util.List;
import java.util.Map;

public class Variable<T extends Number> implements CommonExpression<T> {
    private String var;
    private static final Map<String, Integer> VARIABLES = Map.of(
            "x", 0,
            "y", 1,
            "z", 2
    );

    public Variable(String var) {
        this.var = var;
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
        return List.of(x, y, z).get(VARIABLES.get(var));
    }
}