package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.HashMap;
import java.util.Map;

public class PlatformComponentTemplate implements GameComponentTemplate<PlatformComponent> {

    private GameObject owner;

    public PlatformComponentTemplate(GameObject aOwner) {
        owner = aOwner;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public PlatformComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        PlatformComponent theSpriteComponent = new PlatformComponent(aInstance, aGameRuntime);
        theSpriteComponent.registerEvents(aGameRuntime);
        return theSpriteComponent;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PlatformComponent.TYPE_ATTRIBUTE, PlatformComponent.TYPE);
        return theResult;
    }

    public static PlatformComponentTemplate deserialize(GameObject aObject, Map<String, Object> aSerializedData) {
        return new PlatformComponentTemplate(aObject);
    }

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }
}
