package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JS;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class JSONMap implements Map<String, Object> {

    private interface JSODelegate extends JSObject {
        @JSIndexer
        JSObject get(String aIndex);
    }

    private final JSODelegate root;

    public JSONMap(JSObject aRoot) {
        root = (JSODelegate) aRoot;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object aKey) {
        JSObject theResult = root.get((String) aKey);
        switch(JS.getType(theResult)) {
            case BOOLEAN:
                return JS.unwrapBoolean(theResult);
            case FUNCTION:
                throw new IllegalArgumentException();
            case NUMBER:
                return JS.unwrapInt(theResult);
            case OBJECT:
                return new JSONMap(theResult);
            case STRING:
                return JS.unwrapString(theResult);
            case UNDEFINED:
                return null;
        }
        throw new IllegalStateException();
    }

    @Override
    public Object put(String key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object remove(Object key) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void clear() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object replace(String key, Object value) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new IllegalArgumentException("Not implemented");
    }
}
