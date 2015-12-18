package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeaVMMap implements Map<String, Object> {

    public abstract static class JSDelegate implements JSObject {

        @JSIndexer
        public abstract JSObject get(String aKey);

        @JSBody(
                params = {"aObject"},
                script = "return typeof aObject;"
        )
        public abstract String getTypeOf(JSObject aObject);

        @JSBody(
                params = {"aObject"},
                script = "return Object.prototype.toString.call(aObject) === '[object Array]';"
        )
        public abstract boolean isArray(JSObject aObject);
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

    private Object unwrap(JSObject aValue) {
        if (aValue == null) {
            return null;
        }
        switch (source.getTypeOf(aValue)) {
        case "string":
            return ((JSString) aValue).stringValue();
        case "undefined":
            return null;
        case "number":
            return ((JSNumber) aValue).intValue();
        case "boolean":
            return ((JSBoolean) aValue).booleanValue();
        case "object":
            if (source.isArray(aValue)) {
                JSArray theArray = (JSArray) aValue;
                List<Object> theResult = new ArrayList<>();
                for (int i=0;i<theArray.getLength();i++) {
                    theResult.add(unwrap(theArray.get(i)));
                }
                return theResult;
            } else {
                // Normales Objekt
                return new TeaVMMap((JSDelegate) aValue.cast());
            }
        default:
            throw new RuntimeException("Not implemented type conversion : " + source.getTypeOf(aValue));
        }
    }

    @Override
    public Object get(Object key) {
        return unwrap(source.get((String) key));
    }

    @Override
    public Object put(String key, Object value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Object remove(Object key) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
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
    public Collection<Object> values() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        throw new RuntimeException("Not implemented");
    }
}