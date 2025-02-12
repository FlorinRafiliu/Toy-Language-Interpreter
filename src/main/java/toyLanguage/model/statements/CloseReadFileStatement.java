package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.expressions.IExpression;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.StringType;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFileStatement implements IStatement {
    private IExpression expression;

    public CloseReadFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        IValue res = expression.evaluate(state.getSymTable(), state.getHeap());
        if(!(res.getType().equals(new StringType()))) {
            throw new StatementException("Expression is not a string");
        }
        if(!(state.getFileTable().contains((StringValue) res))) {
            throw new StatementException("File not found");
        }
        BufferedReader bf = null;
        bf = state.getFileTable().get((StringValue) res);
        try {
            bf.close();
        } catch (IOException e) {
            throw new StatementException("Could not close file");
        }
        state.getFileTable().remove((StringValue) res);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseReadFileStatement(expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType type = expression.typeCheck(typeEnv);
        if(type.equals(new StringType()))
            return typeEnv;
        else
            throw new TypeException("Close File: expression is not a String type");
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression.toString() + ")";
    }
}
