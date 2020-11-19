package expression;

import expression.exceptions.DivisionByZeroException;

import java.util.Map;

public class NotCheckedShort implements Calculator<Short> {

    @Override
    public Short add(Short first, Short second) {
        return (short) (first + second);
    }

    @Override
    public Short divide(Short first, Short second) {
        if (second == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return (short) (first/second);
    }

    @Override
    public Short negate(Short first) {
        return (short) (-first);
    }

    @Override
    public Short multiply(Short first, Short second) {
        return (short) (first * second);
    }

    @Override
    public Short subtract(Short first, Short second) {
        return (short) (first - second);
    }

    @Override
    public Short parse(String toParse) {
        return (short) Integer.parseInt(toParse);
    }

    @Override
    public Short min(Short first, Short second) {
        return (short) Math.min(first, second);
    }

    @Override
    public Short max(Short first, Short second) {
        return (short) Math.max(first, second);
    }

    @Override
    public Short count(Short first) {
        return (short) Integer.bitCount(0xffff & first);
    }
}
