package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.HashMap;
import java.util.Map;

public class PlatformComponentTemplate implements GameComponentTemplate<PlatformComponent> {

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

    public static PlatformComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        return new PlatformComponentTemplate();
    }

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }
}
