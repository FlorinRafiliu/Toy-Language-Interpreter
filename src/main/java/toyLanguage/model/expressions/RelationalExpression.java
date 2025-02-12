package toyLanguage.model.expressions;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.type.BoolType;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.value.BoolValue;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.IntValue;

public class RelationalExpression implements IExpression {
    private IExpression left;
    private IExpression right;
    private RelationalOperation operation;

    public RelationalExpression(IExpression left, RelationalOperation operation, IExpression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    @Override
    public IValue evaluate(MyIDictionary<String, IValue> symTable, MyIHeap<Integer, IValue> heap) throws ExpressionException {
        IValue leftRes = this.left.evaluate(symTable, heap);
        IValue rightRes = this.right.evaluate(symTable, heap);

        if(!(leftRes.getType().equals(rightRes.getType()) && leftRes.getType().equals(new IntType()))) {
            throw new ExpressionException("Expressions must be integers!");
        }

        int left = ((IntValue)leftRes).getValue();
        int right = ((IntValue)rightRes).getValue();

        switch (operation) {
            case LESS_THAN -> {
                return new BoolValue(left < right);
            }
            case LESS_THAN_OR_EQUAL -> {
                return new BoolValue(left <= right);
            }
            case EQUAL -> {
                return new BoolValue(left == right);
            }
            case NOT_EQUAL -> {
                return new BoolValue(left != right);
            }
            case GREATER_THAN -> {
                return new BoolValue(left > right);
            }
            case GREATER_THAN_OR_EQUAL -> {
                return new BoolValue(left >= right);
            }
            default -> {
                throw new ExpressionException("Invalid Operation");
            }
        }
    }

    @Override
    public IExpression deepCopy() {
        return new RelationalExpression(left.deepCopy(), operation, right.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType type1, type2;
        type1 = left.typeCheck(typeEnv);
        type2 = right.typeCheck(typeEnv);

        if(type1.equals(new IntType())) {
            if(type2.equals(new IntType())) {
                return new BoolType();
            } else {
                throw new TypeException("second operand is not an integer");
            }
        } else {
            throw new TypeException("first operand is not an integer");
        }
    }

    private String enumToString() {
        switch (operation) {
            case LESS_THAN -> {
                return "<";
            }
            case LESS_THAN_OR_EQUAL -> {
                return "<=";
            }
            case EQUAL -> {
                return "==";
            }
            case NOT_EQUAL -> {
                return "!=";
            }
            case GREATER_THAN -> {
                return ">";
            }
            case GREATER_THAN_OR_EQUAL -> {
                return ">=";
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
