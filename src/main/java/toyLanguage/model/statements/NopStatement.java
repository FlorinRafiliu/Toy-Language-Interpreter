package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;

public class NopStatement implements IStatement {
    public NopStatement(){};

    @Override
    public PrgState execute(PrgState state) {
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NopStatement();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        return typeEnv;
    }


    @Override
    public String toString() {
        return "NopStatement";
    }
}
