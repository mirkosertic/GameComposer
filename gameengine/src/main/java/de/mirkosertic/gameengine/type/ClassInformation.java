package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassInformation {

    private final Map<String, Field> fields;

    protected ClassInformation() {
        fields = new HashMap<String, Field>();
    }

    protected Field register(Field aField) {
        return fields.put(aField.getName(), aField);
    }

    public Field[] getFields() {
        return fields.values().toArray(new Field[fields.size()]);
    }

    public Field getFieldByName(String aFieldName) {
        return fields.get(aFieldName);
    }
}
