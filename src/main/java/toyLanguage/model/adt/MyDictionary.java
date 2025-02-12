package toyLanguage.model.adt;

import toyLanguage.exceptions.ExpressionException;

import java.util.*;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private Map<K, V> map;

    public MyDictionary() {
        map = new HashMap<K, V>();
    }

    @Override
    public void insert(K key, V value) {
        this.map.put(key, value);
    }

    @Override
    public void remove(K key) throws ExpressionException {
        if(!map.containsKey(key)) {
            throw new ExpressionException("Dictionary: key not found!");
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
            throw new ExpressionException("Dictionary: key not found!");
        }
        return map.get(key);
    }

    @Override
    public Set<K> keys() {
        return map.keySet();
    }

    @Override
    public Map<K, V> getContent() {
        return this.map;
    }

    @Override
    public MyIDictionary<K, V> deepCopy() {
        MyDictionary<K, V> newDictionary =  new MyDictionary<K, V>();
        newDictionary.map.putAll(this.map);
        return  newDictionary;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(K e : map.keySet()) {
            str.append(e + " --> " + map.get(e));
            str.append("\n");
        }
        if(str.isEmpty())
            return "SymTable:\n";
        return "SymTable:\n" + str.toString();
    }

}
