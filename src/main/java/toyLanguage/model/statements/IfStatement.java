package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.expressions.IExpression;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.BoolType;
import toyLanguage.model.type.IType;
import toyLanguage.model.value.BoolValue;
import toyLanguage.model.value.IValue;

public class IfStatement implements IStatement {
    private IExpression condition;
    private IStatement thenStatement;
    private IStatement elseStatement;

    public IfStatement(IExpression condition, IStatement thenStatement, IStatement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(condition.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType type = condition.typeCheck(typeEnv);
        if(type.equals(new BoolType())) {
            thenStatement.typecheck(typeEnv.deepCopy());
            elseStatement.typecheck(typeEnv.deepCopy());

            return typeEnv;
        } else {
            throw new TypeException("The condition of IF has not the type bool");
        }
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        IValue val = condition.evaluate(state.getSymTable(), state.getHeap());

        if(!val.getType().equals(new BoolType())) {
            throw new StatementException("The condition is not a boolean");
        }
        if(((BoolValue)val).getValue()) {
            state.getExecStack().push(thenStatement);
        } else {
            state.getExecStack().push(elseStatement);
        }
        return null;
    }

    @Override
    public String toString() {
        return "(IF(" + condition.toString() + ") THEN {" + thenStatement.toString() + "} ELSE { " + elseStatement.toString() + "})";
    }
}
