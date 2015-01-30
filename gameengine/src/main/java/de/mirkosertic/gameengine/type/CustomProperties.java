package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomProperties implements Reflectable {

    private static final CustomPropertiesClassInformation CIINSTANCE = new CustomPropertiesClassInformation();

    private final Map<String, String> values;

    public CustomProperties() {
        values = new HashMap<>();
    }

    @Override
    public CustomPropertiesClassInformation getClassInformation() {
        return CIINSTANCE;
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

        List<Map<String, String>> theEntries = new ArrayList<>();
        for (Map.Entry<String, String> theEntry : values.entrySet()) {
            Map<String, String> theData = new HashMap<>();
            theData.put("key", theEntry.getKey());
            theData.put("value", theEntry.getValue());
            theEntries.add(theData);
        }
        theResult.put("data", theEntries);

        return theResult;
    }

    public static CustomProperties deserialize(Map<String, Object> aData) {

        CustomProperties theProperties = new CustomProperties();
        List<Map<String, String>> theEntries = (List<Map<String, String>>) aData.get("data");
        if (theEntries != null) {
            for (Map<String, String> theEntry : theEntries) {
                String theKey = theEntry.get("key");
                String theValue = theEntry.get("value");
                theProperties.set(theKey, theValue);
            }
        }
        return theProperties;
    }
}
