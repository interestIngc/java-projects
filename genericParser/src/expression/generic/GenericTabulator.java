package expression.generic;

import expression.*;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;
import expression.exceptions.TabulatorException;
import expression.parser.ExpressionParser;
import java.util.Map;

public class GenericTabulator implements Tabulator {
    private Map<String, Calculator<? extends Number>> calculators = Map.of(
            "i", new IntegerCalculator(),
            "d", new DoubleCalculator(),
            "bi", new BigIntegerCalculator(),
            "l", new NotCheckedLong(),
            "u", new NotCheckedInteger(),
            "s", new NotCheckedShort()
    );

    private <T extends Number> Object[][][] table(Calculator<T> calculator, String expr, int x1, int x2, int y1,
                                                  int y2, int z1, int z2) {
        Object[][][] answer = new Object[x2 + 1 - x1][y2 + 1 - y1][z2 + 1 - z1];
        ExpressionParser<T> parser = new ExpressionParser<>(calculator);
        GenericTripleExpression<T> first = parser.parse(expr);
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        answer[i - x1][j - y1][k - z1] = first.evaluate(calculator.parse(Integer.toString(i)),
                                calculator.parse(Integer.toString(j)),
                                calculator.parse(Integer.toString(k)));
                    } catch (OverflowException | DivisionByZeroException e) {
                        answer[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return answer;
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        try {
            return table(calculators.get(mode), expression, x1, x2, y1, y2, z1, z2);
        } catch (NullPointerException e) {
            throw new TabulatorException("mode " + mode +  " is invalid");
        }

    }
}
