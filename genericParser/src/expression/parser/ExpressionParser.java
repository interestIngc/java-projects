package expression.parser;

import expression.*;

import java.util.Map;
import java.util.Set;

public class ExpressionParser<T extends Number> extends BaseParser<T> implements Parser<T> {
    private Calculator<T> calculator;
    public ExpressionParser(Calculator<T> calculator) {
        this.calculator = calculator;
    }
    private final int bracketLevel = -1;


    private final Set<Character> oneDigitOperation = Set.of(
            '+',
            '-',
            '*',
            '/'
    );


    private final Map<Character, Integer> getLevel = Map.of(
            '+', 1,
            '*', 2,
            '-', 1,
            '/', 2,
            'm', 0
    );


    private AbstractBinaryOperation<T> createTwoVar(String c, CommonExpression<T> firstExpression, CommonExpression<T> secondExpression) {
        switch (c) {
            case "+":
                return new Add<>(firstExpression, secondExpression, calculator);
            case "-":
                return new Subtract<>(firstExpression, secondExpression, calculator);
            case "*":
                return new Multiply<>(firstExpression, secondExpression, calculator);
            case "/":
                return new Divide<>(firstExpression, secondExpression, calculator);
            case "max":
                return new Max<>(firstExpression, secondExpression, calculator);
            case "min":
                return new Min<>(firstExpression, secondExpression, calculator);
            default:
                return null;
        }
    }

    @Override
    public GenericTripleExpression<T> parse(String expression) {
        newSource(new StringSource(expression));
        nextChar();
        GenericTripleExpression<T> ans = parse(bracketLevel);
        if (ch != '\0') {
            throw new StringIndexOutOfBoundsException();
        }
        return ans;
    }

    public void nextChar() {
        super.nextChar();
        whitespace();
    }

    public void whitespace() {
        while (Character.isWhitespace(ch)) {
            super.nextChar();
        }
    }
    private CommonExpression<T> parse(int previousLevel) {
        whitespace();
        CommonExpression<T> temp = parseValue();
        while (!testSymbol(previousLevel)) {
            temp = parseSymbol(temp);
        }
        return temp;
    }

    private boolean testSymbol(int previousLevel) {
        if (!(oneDigitOperation.contains(ch) || ch == 'm')) {
            return ch == ')' || ch == '\0';
        }
        return (getLevel.get(ch) <= previousLevel);
    }


    private CommonExpression<T> parseValue() {
        if (test('-')) {
            if (between('0', '9')) {
                return parseNumber(true);
            } else {
                return new Negate<>(parseValue(), calculator);
            }
        } else if (between('0', '9')) {
            return parseNumber(false);
        } else if (between('x', 'z')) {
            String s = Character.toString(ch);
            nextChar();
            return new Variable<T>(s);
        } else if (test('(')) {
            CommonExpression<T> temp = parse(bracketLevel);
            nextChar();
            return temp;
        } else if (ch == 'c') {
            expect("count");
            return new Count<>(parseValue(), calculator);
        }
        return null;
    }

    private CommonExpression<T> parseNumber(boolean isMinus) {
        StringBuilder numb = new StringBuilder();
        if (isMinus) {
            numb.append("-");
        }
        while (between('0', '9')) {
            numb.append(ch);
            super.nextChar();
        }
        if (Character.isWhitespace(ch)) {
            nextChar();
        }
        return new Const<>(calculator.parse(numb.toString()), calculator);
    }

    private String parseFunction() {
        StringBuilder stringbuilder = new StringBuilder();
        while (Character.isLetter(ch)) {
            stringbuilder.append(ch);
            super.nextChar();
        }
        return stringbuilder.toString();
    }

    private CommonExpression<T> parseSymbol(CommonExpression<T> past) {
        String function;
        if (ch == 'm') {
            function = parseFunction();
            return createTwoVar(function, past, parse(getLevel.get('m')));
        } else {
            for (char temp : oneDigitOperation) {
                if (ch == temp) {
                    function = Character.toString(ch);
                    nextChar();
                    return createTwoVar(function, past, parse(getLevel.get(temp)));
                }
            }
        }
        return null;
    }
}