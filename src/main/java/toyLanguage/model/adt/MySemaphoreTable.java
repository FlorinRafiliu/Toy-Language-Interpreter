package toyLanguage.model.adt;

import toyLanguage.exceptions.ExpressionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MySemaphoreTable <K, V> implements MyISemaphoreTable <K, V> {
    private int address;
    private Map <K, V> map;

    public MySemaphoreTable() {
        address = 0;
        map = new HashMap<K, V>();
    }

    @Override
    public void insert(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void remove(K key) throws ExpressionException {
        if(!map.containsKey(key)) {
            throw new ExpressionException("Not found!");
        }
        this.map.remove(key);
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public V get(K key) throws ExpressionException {
        if(!map.containsKey(key)) {
            throw new ExpressionException("Not found!");
        }
        return map.get(key);
    }

    @Override
    public Set<K> keys() {
        return map.keySet();
    }

    @Override
    public int newAddress() {
        address += 1;
        return address;
    }

    @Override
    public void setContent(Map<K, V> map) {
        this.map.clear();
        for(K key : map.keySet()) {
            this.map.put(key, map.get(key));
        }
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(K e : map.keySet()) {
            str.append(e + " --> " + map.get(e));
            str.append("\n");
        }
        if(str.isEmpty())
            return "SemaphoreTable:\n";
        return "SemaphoreTable:\n" + str.toString();
    }
}
