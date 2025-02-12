package toyLanguage.model.expressions;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.type.IType;
import toyLanguage.model.value.IValue;

public class VariableExpression implements IExpression {
    private String variable;

    public VariableExpression(String variable) {
        this.variable = variable;
    }

    @Override
    public IValue evaluate(MyIDictionary<String, IValue> symTable, MyIHeap<Integer, IValue> heap) throws ExpressionException {
        return symTable.get(variable);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(variable);
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        try {
            return typeEnv.get(variable);
        } catch (ExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return variable;
    }
}
