package de.mirkosertic.gameengine.physics;

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
        PlatformComponent theResult = PlatformComponent.deserialize(aObjectInstance, aSerializedData, aRuntime);
        return theResult;
    }
}
