package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.types.ResourceName;

public class ObjectValueOrReferenceEncoder {

    public static Map<String, String> encodeValueOrReference(Object aValue) {
        Map<String, String> theResult = new HashMap<String, String>();
        if (aValue instanceof Boolean) {
            theResult.put("type", "Boolean");
            theResult.put("value", Boolean.toString((Boolean) aValue));
        }
        if (aValue instanceof Float) {
            theResult.put("type", "Float");
            theResult.put("value", Float.toString((Float) aValue));
        }
        if (aValue instanceof String) {
            theResult.put("type", "String");
            theResult.put("value", (String) aValue);
        }
        if (aValue instanceof GameKeyCode) {
            theResult.put("type", "GameKeyCode");
            theResult.put("value", ((GameKeyCode) aValue).name());
        }
        if (aValue instanceof GameObject) {
            theResult.put("type", "GameObject");
            theResult.put("ref", ((GameObject) aValue).uuidProperty().get());
        }
        if (aValue instanceof GameObjectInstance) {
            theResult.put("type", "GameObjectInstance");
            theResult.put("ref", ((GameObjectInstance) aValue).uuidProperty().get());
        }
        if (aValue instanceof ResourceName) {
            theResult.put("type", "ResourceName");
            theResult.put("value", ((ResourceName) aValue).name);
        }
        return theResult;
    }

    public static Object decodeValueOrReference(GameScene aGameScene, Map<String, Object> aValue) {
        String theType = (String) aValue.get("type");
        if ("String".equals(theType)) {
            return aValue.get("value");
        }
        if ("Boolean".equals(theType)) {
            return Boolean.valueOf((String) aValue.get("value"));
        }
        if ("Float".equals(theType)) {
            return Float.valueOf((String) aValue.get("value"));
        }
        if ("GameKeyCode".equals(theType)) {
            return GameKeyCode.valueOf((String) aValue.get("value"));
        }
        if ("GameObject".equals(theType)) {
            return aGameScene.findGameObjectByID((String) aValue.get("ref"));
        }
        if ("GameObjectInstance".equals(theType)) {
            return aGameScene.findGameObjectInstanceByID((String) aValue.get("ref"));
        }
        if ("ResourceName".equals(theType)) {
            return new ResourceName((String) aValue.get("value"));
        }

        return null;
    }
}
