package expression;


import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerCalculator implements Calculator<Integer> {

    @Override
    public Integer add(Integer first, Integer second) {
        String exception = first.toString() + " + " + second.toString() + " is overflowing";
        if (second >= 0) {
            if (first > Integer.MAX_VALUE - second) {
                throw new OverflowException(exception);
            } else {
                return first + second;
            }
        }
        else {
            if (first < Integer.MIN_VALUE - second) {
                throw new OverflowException(exception);
            } else {
                return first + second;
            }
        }
    }

    @Override
    public Integer divide(Integer first, Integer second) {
        if (second == -1 && first == Integer.MIN_VALUE) {
            throw new OverflowException(first.toString() + " / " + second.toString() + " is overflowing");
        }
        if (second == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return first/second;
    }

    @Override
    public Integer negate(Integer first) {
        if (first == Integer.MIN_VALUE) {
            throw new OverflowException("-" + first.toString() + " is overflowing");
        }
        return -first;
    }

    @Override
    public Integer multiply(Integer first, Integer second) {
        if (second == 0 || first == 0) {
            return 0;
        }
        Integer result = first * second;
        if (first != result/second || second != result/first) {
            throw new OverflowException(first.toString() + " * " + second.toString() + " is overflowing");
        }
        return first * second;
    }

    @Override
    public Integer subtract(Integer first, Integer second) {
        if (first < Integer.MIN_VALUE + second && second >= 0) {
            throw new OverflowException(first.toString() + " - " + second.toString() + " is overflowing");
        }
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