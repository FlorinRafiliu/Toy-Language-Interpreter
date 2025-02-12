package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;

public class CompStatement implements IStatement {
    private IStatement firstStatement;
    private IStatement secondStatement;

    public CompStatement(IStatement firstStatement, IStatement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    public PrgState execute(PrgState state) {
        state.getExecStack().push(this.secondStatement);
        state.getExecStack().push(this.firstStatement);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CompStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        MyIDictionary<String, IType> typEnv1 = firstStatement.typecheck(typeEnv);
        MyIDictionary<String, IType> typEnv2 = secondStatement.typecheck(typEnv1);
        return typEnv2;
    }

    public String toString() {
        return firstStatement.toString() + ";" + secondStatement.toString();
    }
}
