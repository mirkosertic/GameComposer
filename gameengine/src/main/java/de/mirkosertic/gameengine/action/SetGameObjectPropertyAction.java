package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.ObjectValueOrReferenceEncoder;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SetGameObjectPropertyAction implements Action {

    public static final String TYPE_VALUE = "SetGameObjectPropertyAction";

    private final Property<GameObject> gameObject;
    private final Property<String> propertyName;
    private final Property<Object> propertyValue;

    public SetGameObjectPropertyAction() {
        gameObject = new Property<GameObject>(this, "gameObject", (GameObject) null);
        propertyName = new Property<String>(this, "propertyName", (String) null);
        propertyValue = new Property<Object>(this, "propertyValue", (Object) null);
    }

    @Override
    public void invoke(GameScene aScene) {
        GameObject theObject = gameObject.get();
        theObject.setPropertyByName(propertyName.get(), propertyValue.get());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("gameObject", gameObject.get().uuidProperty().get());
        theResult.put("propertyName", propertyName.get());
        theResult.put("propertyValue", ObjectValueOrReferenceEncoder.encodeValueOrReference(propertyValue.get()));
        return theResult;
    }

    public static SetGameObjectPropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetGameObjectPropertyAction theResult = new SetGameObjectPropertyAction();
        theResult.gameObject.setQuietly(aGameScene.findGameObjectByID((String) aSerializedData.get("gameObject")));
        theResult.propertyName.setQuietly((String) aSerializedData.get("propertyName"));
        Map<String, Object> thePropertyValue = (Map<String, Object>) aSerializedData.get("propertyValue");
        theResult.propertyValue.setQuietly(ObjectValueOrReferenceEncoder.decodeValueOrReference(aGameScene, thePropertyValue));
        return theResult;
    }
}