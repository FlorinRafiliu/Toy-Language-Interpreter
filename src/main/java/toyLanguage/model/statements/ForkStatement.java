package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyStack;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;

public class ForkStatement implements IStatement {
    private IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {

        PrgState prg = new PrgState(statement, new MyStack<IStatement>(), state.getSymTable().deepCopy(), state.getOutputList(),
                                    state.getFileTable(), state.getHeap(), state.getLatchTable(), state.getSemaphoreTable());
        return prg;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}
