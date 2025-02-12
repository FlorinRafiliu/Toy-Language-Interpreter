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

public class WhileStatement implements IStatement {
    private IExpression expression;
    private IStatement instruction;
    public WhileStatement(IExpression expression, IStatement instruction) {
        this.expression = expression;
        this.instruction = instruction;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException {
        IValue res = expression.evaluate(state.getSymTable(), state.getHeap());

        if(!(res.getType().equals(new BoolType()))) {
            throw new ExpressionException("Condition is not boolean");
        }

        boolean condition = ((BoolValue) res).getValue();

        if(condition == true) {
            state.getExecStack().push(this.deepCopy());
            state.getExecStack().push(instruction.deepCopy());
        }

        return null;
    }

    @Override
    public String toString() {
        return "while(" + expression.toString() + ") {" + instruction.toString() + "}";
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(), instruction.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ExpressionException {
        IType type = expression.typeCheck(typeEnv);
        if(type.equals(new BoolType())) {
            instruction.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new TypeException("The condition of IF has not the type bool");
        }
    }
}
