package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.expressions.IExpression;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.type.StringType;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.IntValue;
import toyLanguage.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement {
    private IExpression filename;
    private String varName;

    public ReadFileStatement(IExpression filename, String varName) {
        this.filename = filename;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        if(!state.getSymTable().contains(varName)) {
            throw new StatementException("File does not exist");
        }
        if(!(state.getSymTable().get(varName).getType().equals(new IntType()))) {
            throw new StatementException("The variable is not an int type");
        }

        IValue res = filename.evaluate(state.getSymTable(), state.getHeap());
        if(!res.getType().equals(new StringType())) {
            throw new StatementException("The evaluate filename is not a string type");
        };

        if(!(state.getFileTable().contains((StringValue)res))) {
            throw new StatementException("The file does not exist");
        }

        BufferedReader bufferedReader = null;
        bufferedReader = state.getFileTable().get((StringValue) res);

        try {
            String readRes = bufferedReader.readLine();
            if(readRes == null) {
                readRes = "0";
            }

            int num = Integer.parseInt(readRes);
            state.getSymTable().insert(varName, new IntValue(num));

        } catch (IOException e) {
            throw new StatementException(e.getMessage());
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(filename.deepCopy(), varName);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType fileType = filename.typeCheck(typeEnv);
        IType varType = null;
        try {
            varType = typeEnv.get(varName);
        } catch (ExpressionException e) {
            throw new RuntimeException(e);
        }
        if(fileType.equals(new StringType()) && varType.equals(new IntType())) {
            return typeEnv;
        }
        else
            throw new TypeException("Read file: error");
    }

    @Override
    public String toString() {
        return "readFile(" + filename.toString() + ", " + varName + ")";
    }
}
