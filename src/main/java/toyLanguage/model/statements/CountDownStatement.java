package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.value.IntValue;

public class CountDownStatement implements IStatement {
    private String var;

    public CountDownStatement(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        if(!(state.getSymTable().contains(var))) {
            throw new StatementException("CountDown: var is not declared!");
        }
        if(!(state.getSymTable().get(var).getType().equals(new IntType()))) {
            throw new StatementException("CountDown: var is not of intType!");
        }

        int index = ((IntValue) state.getSymTable().get(var)).getValue();

        synchronized (state.getLatchTable()) {
            if(!(state.getLatchTable().contains(index))) {
                throw new StatementException("CountDown: index is not declared in latchTable!");
            }
            int nr = state.getLatchTable().get(index);
            if(nr > 0) {
                state.getLatchTable().insert(index, nr - 1);
            }
            state.getOutputList().add(Integer.toString(state.getId()));
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CountDownStatement(var);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType varType = typeEnv.get(var);
        if(!(varType.equals(new IntType()))) {
            throw new TypeException("CountDown: var is not of type int!");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "countDown(" + var + ")";
    }
}
