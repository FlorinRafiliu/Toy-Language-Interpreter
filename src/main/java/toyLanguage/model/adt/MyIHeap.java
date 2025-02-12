package toyLanguage.model.adt;

import toyLanguage.exceptions.ExpressionException;

import java.util.Map;
import java.util.Set;

public interface MyIHeap<K, V> {
    void insert(K key, V value);
    void remove(K key) throws ExpressionException;
    boolean contains(K key);
    V get(K key) throws ExpressionException;
    Set<K> keys();
    int newAddress();
    void setContent(Map<K, V> map);
    Map <K, V> getContent();
}
