package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassInformation {

    private final Map<String, Field> fields;
    private final Map<String, Method> methods;

    protected ClassInformation() {
        fields = new HashMap<>();
        methods = new HashMap<>();
    }

    protected void register(Field aField) {
        fields.put(aField.getName(), aField);
    }

    protected void register(Method aMethod) {
        methods.put(aMethod.getName(), aMethod);
    }

    public Field[] getFields() {
        return fields.values().toArray(new Field[fields.size()]);
    }

    public Field getFieldByName(String aFieldName) {
        return fields.get(aFieldName);
    }

    public Method getMethodByName(String aMethodName) {
        return methods.get(aMethodName);
    }
}