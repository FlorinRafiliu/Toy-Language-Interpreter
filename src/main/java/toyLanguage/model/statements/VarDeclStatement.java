package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;

public class VarDeclStatement implements IStatement {
    private String varName;
    private IType type;

    public VarDeclStatement(String varName, IType type) {
        this.varName = varName;
        this.type = type;
    }

    public PrgState execute(PrgState state) throws StatementException {
        if(state.getSymTable().contains(varName)) {
            throw new StatementException("Variable is already declared!");
        }
        state.getSymTable().insert(varName, type.getDefaultValue());
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new VarDeclStatement(varName, type);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        typeEnv.insert(varName, type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type.toString() + " " + varName;
    }
}
