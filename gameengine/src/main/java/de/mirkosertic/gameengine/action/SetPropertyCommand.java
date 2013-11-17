package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.ObjectValueOrReferenceEncoder;

import java.util.HashMap;
import java.util.Map;

public class SetPropertyCommand {

    public final String propertyName;
    public final Object propertyValue;

    public SetPropertyCommand(String aName, Object aValue) {
        propertyName = aName;
        propertyValue = aValue;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("propertyName", propertyName);
        theResult.put("propertyValue", ObjectValueOrReferenceEncoder.encodeValueOrReference(propertyValue));
        return theResult;
    }

    public static SetPropertyCommand deserialize(GameScene aGameScene, Map<String, Object> aSerializedData) {
        String thePropertyName = (String) aSerializedData.get("propertyName");
        Map<String, Object> theValue = (Map<String, Object>) aSerializedData.get("propertyValue");
        return new SetPropertyCommand(thePropertyName, ObjectValueOrReferenceEncoder.decodeValueOrReference(aGameScene, theValue));
    }
}