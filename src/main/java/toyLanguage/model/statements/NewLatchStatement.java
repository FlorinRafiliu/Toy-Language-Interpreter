package toyLanguage.model.statements;

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

public class NewLatchStatement implements IStatement {
    private String var;
    private IExpression exp;

    public NewLatchStatement(String var, IExpression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        IValue num1 = exp.evaluate(state.getSymTable(), state.getHeap());

        if(!(num1.getType().equals(new IntType()))) {
            throw new StatementException("Latch: expression is not of intType!");
        }

        if(!(state.getSymTable().contains(var))) {
            throw new StatementException("Latch: var is not declared!");
        }
        if(!(state.getSymTable().get(var).getType().equals(new IntType()))) {
            throw new StatementException("Latch: var is not of intType!");
        }

        synchronized (state.getLatchTable()) {
            int key = state.getLatchTable().newAddress();
            state.getLatchTable().insert(key, ((IntValue) num1).getValue());
            state.getSymTable().insert(var, new IntValue(key));
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NewLatchStatement(var, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType varType = typeEnv.get(var);
        IType expType = exp.typeCheck(typeEnv);
        if(!(varType.equals(expType) && varType.equals(new IntType()))) {
            throw new TypeException("NewLatch: var and/or exp are not of type int!");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLatch(" + var + ", " + exp.toString() + ")";
    }
}
