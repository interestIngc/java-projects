package expression;

import expression.exceptions.DivisionByZeroException;

public class NotCheckedInteger implements Calculator<Integer> {
    @Override
    public Integer add(Integer first, Integer second) {
        return first + second;
    }

    @Override
    public Integer divide(Integer first, Integer second) {
        if (second == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return first/second;
    }

    @Override
    public Integer negate(Integer first) {
        return -first;
    }

    @Override
    public Integer multiply(Integer first, Integer second) {
        return first * second;
    }

    @Override
    public Integer subtract(Integer first, Integer second) {
        return first - second;
    }

    @Override
    public Integer parse(String toParse) {
        return Integer.parseInt(toParse);
    }

    @Override
    public Integer min(Integer first, Integer second) {
        return Math.min(first, second);
    }

    @Override
    public Integer max(Integer first, Integer second) {
        return Math.max(first, second);
    }

    @Override
    public Integer count(Integer first) {
        return Integer.bitCount(first);
    }

}
