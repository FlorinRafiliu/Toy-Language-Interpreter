package toyLanguage.model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    List<T> list;

    public MyList() {
        list = new ArrayList<>();
    }

    @Override
    public List<T> getAll() {
        return list;
    }

    @Override
    public void add(T t) {
        list.add(t);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(T t : list) {
            str.append(t);
            str.append("\n");
        }
        if(str.isEmpty())
            return "Out:\n";

        return "Out:\n" + str.toString();
    }
}
