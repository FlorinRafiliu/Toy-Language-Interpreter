package toyLanguage.model.statements;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.expressions.IExpression;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.type.IType;
import toyLanguage.model.type.RefType;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.RefValue;

public class HeapWritingStatement implements IStatement {
    private String var_name;
    private IExpression expression;

    public HeapWritingStatement(String var_name, IExpression expression) {
        this.var_name = var_name;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        if(!(state.getSymTable().contains(var_name))) {
            throw new StatementException("Variable not found!");
        }
        if(!(state.getSymTable().get(var_name).getType() instanceof RefType)) {
            throw new StatementException("Variable is not Ref Type!");
        }

        int address = 0;
        address = ((RefValue) state.getSymTable().get(var_name)).getAddress();
        if(!(state.getHeap().contains(address))) {
            throw new StatementException("Invalid Address");
        }

        IValue res = expression.evaluate(state.getSymTable(), state.getHeap());

        if(!(res.getType().equals(((RefType) state.getSymTable().get(var_name).getType()).getInner()))) {
            throw new StatementException("Invalid type");
        }

        state.getHeap().insert(address, res);

        return null;
    }

    @Override
    public String toString() {
        return "wH(" + var_name + ", " + expression.toString() + ")";
    }

    @Override
    public IStatement deepCopy() {
        return new HeapWritingStatement(var_name, expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType varType = null;
        try {
            varType = typeEnv.get(var_name);
        } catch (ExpressionException e) {
            throw new RuntimeException(e);
        }
        IType expType = expression.typeCheck(typeEnv);

        if(varType.equals(new RefType(expType))) {
            return typeEnv;
        } else {
            throw new TypeException("wH stmt: right hand side and left hand side have different types");
        }
    }
}
