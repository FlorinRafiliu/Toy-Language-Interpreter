package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.expressions.IExpression;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.value.IValue;

public class AssignStatement implements IStatement {
    private String id;
    private IExpression expression;

    public AssignStatement(String id, IExpression expression) {
        this.id = id;
        this.expression = expression;
    }


    @Override
    public String toString() {
        return id + "=" + expression.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        if(!state.getSymTable().contains(id)) {
            throw new StatementException("Variable Id is not declared!");
        }
        IValue val = expression.evaluate(state.getSymTable(), state.getHeap());
        if(!val.getType().equals(state.getSymTable().get(id).getType())) {
            throw new StatementException("Type of expression and type of variable do not match!");
        }
        state.getSymTable().insert(id, val);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignStatement(id, expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType typeVar = null;
        try {
            typeVar = typeEnv.get(id);
        } catch (ExpressionException e) {
            throw new RuntimeException(e);
        }
        IType typExp = expression.typeCheck(typeEnv);
        if (typeVar.equals(typExp))
            return typeEnv;
        else
            throw new TypeException("Assignment: right hand side and left hand side have different types ");
    }

}