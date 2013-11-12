package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

public class ResourceName {

    public final String name;

    public ResourceName(String aName) {
        name = aName;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("name", name);
        return theResult;
    }

    public static ResourceName deserialize(Map<String, Object> aSerializedData) {
        return new ResourceName((String) aSerializedData.get("name"));
    }
}
