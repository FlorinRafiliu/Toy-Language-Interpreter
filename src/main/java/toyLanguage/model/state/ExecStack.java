package toyLanguage.model.state;

import toyLanguage.exceptions.StackException;
import toyLanguage.model.adt.MyIStack;
import toyLanguage.model.adt.MyStack;
import toyLanguage.model.statements.IStatement;

public class ExecStack implements IExecStack {
    private MyIStack<IStatement> stack;
    public ExecStack() {
        stack = new MyStack<IStatement>();
    }

    @Override
    public void push(IStatement statement) {
        stack.push(statement);
    }

    @Override
    public IStatement pop() throws StackException {
        return stack.pop();
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        return stack.toString();
    }

    public MyStack<IStatement> getStack() {
        return (MyStack<IStatement>) this.stack;
    }
}
