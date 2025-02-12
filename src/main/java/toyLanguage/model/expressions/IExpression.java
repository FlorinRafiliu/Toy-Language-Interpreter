package toyLanguage.model.expressions;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.type.IType;
import toyLanguage.model.value.IValue;

public interface IExpression {
    IValue evaluate(MyIDictionary<String, IValue> symTable, MyIHeap<Integer, IValue> heap) throws ExpressionException;
    IExpression deepCopy();
    IType typeCheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException;
}
