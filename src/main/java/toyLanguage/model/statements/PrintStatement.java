package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.expressions.IExpression;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.value.IValue;

public class PrintStatement implements IStatement {
    private IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ExpressionException {
        IValue val = expression.evaluate(state.getSymTable(), state.getHeap());
        state.getOutputList().add(val.toString());
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + expression + ")";
    }
}
