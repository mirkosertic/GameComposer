package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class TeaVMMap<V> implements Map<String, V> {

    public abstract class JSDelegate implements JSObject {

        @JSBody(
                params = {"aSource", "aKey"},
                script = "return aSource[aKey];"
        )
        public abstract JSObject get(JSObject aSource, String aKey);
    }

    private final JSDelegate source;

    public TeaVMMap(JSDelegate aSource) {
        source = aSource;
    }

    @Override
    public int size() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean isEmpty() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean containsKey(Object key) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean containsValue(Object value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public V get(Object key) {
        return source.get(source, (String) key).cast();
    }

    @Override
    public V put(String key, V value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public V remove(Object key) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Set<String> keySet() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<V> values() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        throw new RuntimeException("Not implemented");
    }
}
