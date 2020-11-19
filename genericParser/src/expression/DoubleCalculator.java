package expression;

import com.sun.jdi.LongValue;

public class DoubleCalculator implements Calculator<Double> {

    @Override
    public Double add(Double first, Double second) {
        return first + second;
    }

    @Override
    public Double divide(Double first, Double second) {
        return first/second;
    }

    @Override
    public Double negate(Double first) {
        return -first;
    }

    @Override
    public Double multiply(Double first, Double second) {
        return first * second;
    }

    @Override
    public Double subtract(Double first, Double second) {
        return first - second;
    }

    @Override
    public Double parse(String toParse) {
        return Double.parseDouble(toParse);
    }

    @Override
    public Double min(Double first, Double second) {
        return Double.min(first, second);
    }

    @Override
    public Double max(Double first, Double second) {
        return Double.max(first, second);
    }

    @Override
    public Double count(Double first) {
        double res = Long.bitCount(Double.doubleToLongBits(first));
        return res;
    }
}