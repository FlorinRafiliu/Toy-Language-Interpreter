package toyLanguage.model.statements;

import javafx.util.Pair;
import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.expressions.IExpression;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.IntValue;

import java.util.ArrayList;
import java.util.List;

public class CreateSemaphore implements IStatement{
    private String var;
    private IExpression exp1;

    public CreateSemaphore(String var, IExpression exp1) {
        this.var = var;
        this.exp1 = exp1;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        IValue res = exp1.evaluate(state.getSymTable(), state.getHeap());

        if(!(res.getType().equals(new IntType()))) {
            throw new ExpressionException("Expression is not int type!");
        }

        try {
            if(!(state.getSymTable().get(var).getType().equals(new IntType()))) {
                throw new StatementException("Type of variable is not Int!");
            }
        } catch (ExpressionException e) {
            throw new RuntimeException(e);
        }

        synchronized (state.getSemaphoreTable()) {
        int key = state.getSemaphoreTable().newAddress();
        state.getSemaphoreTable().insert(key, new Pair <Integer, List<Integer>> (((IntValue) res).getValue(), new ArrayList<>()));
        state.getSymTable().insert(var, new IntValue(key));
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CreateSemaphore(var, exp1.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType typeVar = typeEnv.get(var);
        IType typExp = exp1.typeCheck(typeEnv);
        if (typeVar.equals(typExp) && typeVar.equals(new IntType()))
            return typeEnv;
        else
            throw new TypeException("Semaphore: variable and/or expression are not intTypes ");
    }

    @Override
    public String toString() {
        return "createSemaphore(" + var + ", " + exp1.toString() + ")";
    }
}
