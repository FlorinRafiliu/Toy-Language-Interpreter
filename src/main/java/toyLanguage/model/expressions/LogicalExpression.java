package toyLanguage.model.expressions;


import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.type.BoolType;
import toyLanguage.model.type.IType;
import toyLanguage.model.value.BoolValue;
import toyLanguage.model.value.IValue;

public class LogicalExpression implements IExpression {
    private IExpression left;
    private IExpression right;
    private LogicalOperation operation;

    public LogicalExpression(IExpression left, IExpression right, LogicalOperation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public IValue evaluate(MyIDictionary<String, IValue> symTable, MyIHeap<Integer, IValue> heap) throws ExpressionException {
        IValue leftVal = left.evaluate(symTable, heap);
        IValue rightVal = right.evaluate(symTable, heap);
        if(!(leftVal.getType().equals(new BoolType()) && rightVal.getType().equals(new BoolType()))) {
            throw new ExpressionException("The expressions are not booleans");
        }
        boolean resultLeft = ((BoolValue) leftVal).getValue();
        boolean resultRight = ((BoolValue) rightVal).getValue();
        if(operation == LogicalOperation.AND) {
            return new BoolValue(resultLeft && resultRight);
        } else {
            return new BoolValue(resultLeft || resultRight);
        }
    }

    @Override
    public IExpression deepCopy() {
        return new LogicalExpression(left.deepCopy(), right.deepCopy(), operation);
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType t1, t2;
        t1 = left.typeCheck(typeEnv);
        t2 = right.typeCheck(typeEnv);

        if(t1.equals(new BoolType())) {
            if(t2.equals(new BoolType())) {
                return new BoolType();
            } else {
                throw new TypeException("second operand is not Boolean");
            }
        } else {
            throw new TypeException("first operand is not Boolean");
        }
    }

    @Override
    public String toString() {
        return left.toString() + " " + operation.toString().toLowerCase()+ " " + right.toString();
    }
}
