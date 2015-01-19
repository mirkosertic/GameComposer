package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.HashMap;
import java.util.Map;

public class CustomProperties implements Reflectable {

    private final Map<String, String> values;

    public CustomProperties() {
        values = new HashMap<>();
    }

    @Override
    public CustomPropertiesClassInformation getClassInformation() {
        return CustomPropertiesClassInformation.INSTANCE;
    }

    @ReflectiveMethod
    public void set(String aKey, String aValue) {
        if (aValue != null) {
            values.put(aKey, aValue);
        } else {
            remove(aKey);
        }
    }

    @ReflectiveMethod
    public String get(String aKey) {
        return values.get(aKey);
    }

    @ReflectiveMethod
    public void remove(String aKey) {
        values.remove(aKey);
    }

    @ReflectiveMethod
    public boolean has(String aKey) {
        return values.containsKey(aKey);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        for (Map.Entry<String, String> theEntry : values.entrySet()) {
            theResult.put(theEntry.getKey(), theEntry.getValue());
        }
        return theResult;
    }

    public static CustomProperties deserialize(Map<String, Object> aData) {
        CustomProperties theProperties = new CustomProperties();
        for (Map.Entry<String, Object> theEntry : aData.entrySet()) {
            theProperties.set(theEntry.getKey(), (String) theEntry.getValue());
        }
        return theProperties;
    }
}
