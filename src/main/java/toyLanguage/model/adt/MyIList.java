package toyLanguage.model.adt;

import java.util.List;

public interface MyIList<T> {
    List<T> getAll();
    void add(T t);
}
