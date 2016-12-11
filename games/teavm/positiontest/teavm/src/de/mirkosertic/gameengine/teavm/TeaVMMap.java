/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.teavm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

public class TeaVMMap implements Map<String, Object> {

    @JSBody(
            params = {"aObject"},
            script = "return Object.prototype.toString.call(aObject) === '[object Array]';"
    )
    public static native boolean isArray(JSObject aObject);

    public abstract static class JSDelegate implements JSObject {

        @JSBody(
                params = {},
                script = "return {};"
        )
        public static native JSDelegate create();

        @JSIndexer
        public abstract JSObject get(String aKey);

        @JSIndexer
        public abstract void set(String aKey, JSObject aOject);

        @JSBody(
                params = {"aObject"},
                script = "return typeof aObject;"
        )
        public abstract String getTypeOf(JSObject aObject);
    }

    public static JSObject toJS(Map<String, Object> aMap) {
        JSDelegate theResult = JSDelegate.create();
        return fill(aMap, theResult);
    }

    @JSBody(params = { "object" }, script = "return JSON.stringify(object, null, 2);")
    public static native String stringifyPretty(JSObject object);

    private static JSObject convert(Object aObject) {
        if (aObject instanceof String) {
            return JSString.valueOf((String) aObject);
        }
        if (aObject instanceof Boolean) {
            return JSBoolean.valueOf((boolean) aObject);
        }
        if (aObject instanceof Integer) {
            return JSNumber.valueOf((int) aObject);
        }
        if (aObject instanceof Map) {
            JSDelegate theResult = JSDelegate.create();
            fill((Map<String, Object>) aObject, theResult);
            return theResult;
        }
        if (aObject instanceof List) {
            List theList = (List) aObject;
            JSArray theArray = JSArray.create(theList.size());
            for (int i=0;i<theList.size();i++) {
                theArray.set(i, convert(theList.get(i)));
            }
            return theArray;
        }
        throw new IllegalArgumentException("Unbekannter Typ : " + aObject.getClass());
    }

    private static JSDelegate fill(Map<String, Object> aMap, JSDelegate aDelegate) {
        for (Map.Entry<String, Object> theEntry : aMap.entrySet()) {
            Object theValue = theEntry.getValue();
            aDelegate.set(theEntry.getKey(), convert(theValue));
        }
        return aDelegate;
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
            if (isArray(aValue)) {
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