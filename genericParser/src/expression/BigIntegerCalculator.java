package expression;


import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class BigIntegerCalculator implements Calculator<BigInteger> {
    @Override
    public BigInteger add(BigInteger first, BigInteger second) {
        return first.add(second);
    }

    @Override
    public BigInteger divide(BigInteger first, BigInteger second) {
        if (second.equals(BigInteger.valueOf(0))) {
            throw new DivisionByZeroException("division by zero");
        }
        return first.divide(second);
    }

    @Override
    public BigInteger negate(BigInteger first) {
        return first.negate();
    }

    @Override
    public BigInteger multiply(BigInteger first, BigInteger second) {
        return first.multiply(second);
    }

    @Override
    public BigInteger subtract(BigInteger first, BigInteger second) {
        return first.subtract(second);
    }

    @Override
    public BigInteger parse(String toParse) {
        return new BigInteger(toParse);
    }


    @Override
    public BigInteger min(BigInteger first, BigInteger second) {
        return first.min(second);
    }

    @Override
    public BigInteger max(BigInteger first, BigInteger second) {
        return first.max(second);
    }

    @Override
    public BigInteger count(BigInteger first) {
        return BigInteger.valueOf(first.bitCount());
    }
}
