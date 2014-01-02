package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class PlatformComponentUnmarshaller implements GameComponentUnmarshaller<PlatformComponent> {

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }

    @Override
    public PlatformComponent deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return PlatformComponent.deserialize(aObjectInstance, aSerializedData, aRuntime);
    }
}
