package toyLanguage.model.expressions;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.type.IType;
import toyLanguage.model.value.IValue;


public class ValueExpression  implements IExpression {
    private IValue value;
    public ValueExpression(IValue value) {
        this.value = value;
    }
    @Override
    public IValue evaluate(MyIDictionary<String, IValue> symTable, MyIHeap<Integer, IValue> heap) {
        return this.value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        return value.getType();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
