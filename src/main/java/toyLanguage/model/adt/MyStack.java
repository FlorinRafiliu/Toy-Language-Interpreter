package toyLanguage.model.adt;

import toyLanguage.exceptions.StackException;

import java.util.List;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;
    public MyStack() {
        stack = new Stack<T>();
    }

    @Override
    public T pop() throws StackException {
        if (stack.isEmpty()) {
            throw new StackException("Stack is empty!");
        }
        return stack.pop();
    }

    @Override
    public void push(T t) {
        stack.push(t);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public List<T> getAll() {
        return stack.stream().toList();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(T t : stack) {
            str.append(t);
            str.append("\n");
        }
        if(str.isEmpty()) {
            return "ExeStack:\n";
        }
        return "ExeStack:\n" + str.toString();
    }
}
