package toyLanguage.model.state;

import toyLanguage.exceptions.StackException;
import toyLanguage.model.statements.IStatement;

public interface IExecStack {
    void push(IStatement statement);
    IStatement pop() throws StackException;
    int size();
    boolean isEmpty();
}
