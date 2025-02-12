package toyLanguage.model.statements;

import javafx.util.Pair;
import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.value.IntValue;

import java.util.List;

public class ReleaseStatement implements IStatement {
    private String var;

    public ReleaseStatement(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        int key;
        if(!(state.getSymTable().contains(var)))
            throw new StatementException("Variable is not declared");

        try {
            if(!(state.getSymTable().get(var).getType().equals(new IntType()))) {
                throw new StatementException("Type of variable is not Int!");
            }
            key = ((IntValue) state.getSymTable().get(var)).getValue();
        } catch (ExpressionException e) {
            throw new RuntimeException(e);
        }

        synchronized (state.getSemaphoreTable()) {

           if (!(state.getSemaphoreTable().contains(key))) {
               throw new StatementException("Key is not a semaphore!");
           }

           Pair<Integer, List<Integer>> x = state.getSemaphoreTable().get(key);
           Integer N1 = x.getKey();
           List<Integer> list1 = x.getValue();

           if (list1.contains(state.getId())) {
               list1.remove(list1.indexOf(state.getId()));
               state.getSemaphoreTable().insert(key, new Pair<>(N1, list1));
           }

        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ReleaseStatement(var);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType typeVar = typeEnv.get(var);
        if (typeVar.equals(new IntType()))
            return typeEnv;
        else
            throw new TypeException("Release: Variable is not intType");
    }

    @Override
    public String toString() {
        return "release(" + var + ")";
    }
}
