package toyLanguage.model.expressions;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.RefType;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.RefValue;

public class HeapReadingExpression implements IExpression {
    private IExpression expression;

    public HeapReadingExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue evaluate(MyIDictionary<String, IValue> symTable, MyIHeap<Integer, IValue> heap) throws ExpressionException {
        IValue res = expression.evaluate(symTable, heap);
        if(!(res instanceof RefValue)) {
            throw new ExpressionException("Invalid type!");
        }
        int key = ((RefValue) res).getAddress();

        if(!(heap.contains(key))) {
            throw new ExpressionException("Invalid address!");
        }
        return heap.get(key);
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }

    @Override
    public IExpression deepCopy() {
        return new HeapReadingExpression(expression.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType type = expression.typeCheck(typeEnv);
        if (type instanceof RefType) {
            return ((RefType) type).getInner();
        } else
            throw new TypeException("the rH argument is not a Ref Type");
    }
}
