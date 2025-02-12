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
import java.io.FileReader;
import java.io.IOException;

public class OpenReadFileStatement implements IStatement {
    private IExpression expression;

    public OpenReadFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        IValue res = expression.evaluate(state.getSymTable(), state.getHeap());
        if(!(res.getType().equals(new StringType()))) {
            throw new StatementException("Value expression is not a string type");
        }
        if(state.getFileTable().contains((StringValue)res)) {
            throw new StatementException("File already exist");
        }

        try {
            BufferedReader bf = new BufferedReader(new FileReader(((StringValue) res).getValue()));
            state.getFileTable().insert((StringValue) res, bf);
        } catch (IOException e) {
            throw new StatementException("File not found");
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenReadFileStatement(expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType type = expression.typeCheck(typeEnv);
        if(type.equals(new StringType())) {
            return typeEnv;
        }
        else
            throw new TypeException("Open file: expression is not StringType");

    }

    @Override
    public String toString() {
        return "openRFile(" + expression.toString() + ")";
    }
}
