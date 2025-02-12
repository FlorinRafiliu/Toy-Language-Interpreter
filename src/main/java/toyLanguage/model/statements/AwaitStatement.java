package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.value.IntValue;

public class AwaitStatement implements IStatement {
    private String var;

    public AwaitStatement(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        if(!(state.getSymTable().contains(var))) {
            throw new StatementException("Await: var is not declared!");
        }
        if(!(state.getSymTable().get(var).getType().equals(new IntType()))) {
            throw new StatementException("Await: var is not of intType!");
        }

        int index = ((IntValue) state.getSymTable().get(var)).getValue();

        synchronized (state.getLatchTable()) {
            if(!(state.getLatchTable().contains(index))) {
                throw new StatementException("Await: index is not declared in latchTable!");
            }
            if(state.getLatchTable().get(index) > 0) {
                state.getExecStack().push(this.deepCopy());
            }
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(var);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType varType = typeEnv.get(var);
        if(!(varType.equals(new IntType()))) {
            throw new TypeException("Await: var is not of type int!");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }
}
