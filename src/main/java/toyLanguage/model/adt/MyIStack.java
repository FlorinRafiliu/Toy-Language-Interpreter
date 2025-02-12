package toyLanguage.model.adt;

import toyLanguage.exceptions.StackException;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws StackException;
    void push(T t);
    boolean isEmpty();
    int size();
    List <T> getAll();
}
