package otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> caches = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        listeners.forEach(listener -> listener.notify(key, value, "add"));
        caches.put(key, value);
    }

    @Override
    public void remove(K key) {
        listeners.forEach(listener -> listener.notify(key, null,"remove"));
        caches.remove(key);
    }

    @Override
    public V get(K key) {
        listeners.forEach(listener -> listener.notify(key, null,"get"));
        return caches.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);

    }
}
