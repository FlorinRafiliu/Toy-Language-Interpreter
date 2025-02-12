package toyLanguage.model.expressions;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.IntValue;


public class ArithmeticalExpression implements IExpression {
    private IExpression left;
    private IExpression right;
    private ArithmeticalOperation operation;

    public ArithmeticalExpression(IExpression left, ArithmeticalOperation operation, IExpression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    @Override
    public IValue evaluate(MyIDictionary<String, IValue> symTable, MyIHeap<Integer, IValue> heap) throws ExpressionException {
        IValue left = this.left.evaluate(symTable, heap);
        IValue right = this.right.evaluate(symTable, heap);

        if(!(left.getType().equals(new IntType()) && right.getType().equals(new IntType()))) {
            throw new ExpressionException("Invalid Expression");
        }

        int intLeft = ((IntValue) left).getValue();
        int intRight = ((IntValue) right).getValue();

        switch (operation) {
            case PLUS -> {
                return new IntValue(intLeft + intRight);
            }
            case MINUS -> {
                return new IntValue(intLeft - intRight);
            }
            case MULTIPLY -> {
                return new IntValue(intLeft * intRight);
            }
            case DIVIDE -> {
                if(intRight == 0) throw new ExpressionException("Division by zero");
                return new IntValue(intLeft / intRight);
            }
            default -> {
                throw new ExpressionException("Invalid Operation");
            }
        }
    }

    @Override
    public IExpression deepCopy() {
        return new ArithmeticalExpression(left.deepCopy(), operation, right.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType type1, type2;
        type1 = left.typeCheck(typeEnv);
        type2 = right.typeCheck(typeEnv);

        if(type1.equals(new IntType())) {
            if(type2.equals(new IntType())) {
                return new IntType();
            } else {
                throw new TypeException("second operand is not an integer");
            }
        } else {
            throw new TypeException("first operand is not an integer");
        }
    }

    private String enumToString() {
        switch (operation) {
            case PLUS -> {
                return "+";
            }
            case MINUS -> {
                return "-";
            }
            case MULTIPLY -> {
                return "*";
            }
            case DIVIDE -> {
                return "/";
            }
            default -> {
                return "";
            }
        }
    }

    @Override
    public String toString() {
        return left.toString() + " " + enumToString() + " " + right.toString();
    }
}
