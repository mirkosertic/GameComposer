package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class StaticComponentTemplate implements GameComponentTemplate<StaticComponent> {

    private final GameObject owner;

    public StaticComponentTemplate(GameObject aOwner) {
        owner = aOwner;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public StaticComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new StaticComponent(aInstance);
    }

    @Override
    public String getTypeKey() {
        return StaticComponent.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(StaticComponent.TYPE_ATTRIBUTE, StaticComponent.TYPE);
        return theResult;
    }

    public static StaticComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        return new StaticComponentTemplate(aOwner);
    }
}
