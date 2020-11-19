package expression;

import expression.exceptions.DivisionByZeroException;

public class NotCheckedLong implements Calculator<Long> {

    @Override
    public Long add(Long first, Long second) {
        return first + second;
    }

    @Override
    public Long divide(Long first, Long second) {
        if (second == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return first/second;
    }

    @Override
    public Long negate(Long first) {
        return -first;
    }

    @Override
    public Long multiply(Long first, Long second) {
        return first * second;
    }

    @Override
    public Long subtract(Long first, Long second) {
        return first - second;
    }

    @Override
    public Long parse(String toParse) {
        return Long.parseLong(toParse);
    }

    @Override
    public Long min(Long first, Long second) {
        return Math.min(first, second);
    }

    @Override
    public Long max(Long first, Long second) {
        return Math.max(first, second);
    }

    @Override
    public Long count(Long first) {
        return (long) Long.bitCount(first);
    }
}