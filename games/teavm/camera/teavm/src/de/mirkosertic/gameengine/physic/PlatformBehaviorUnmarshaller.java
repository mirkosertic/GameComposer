package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class PlatformBehaviorUnmarshaller implements BehaviorUnmarshaller<PlatformBehavior> {

    @Override
    public String getTypeKey() {
        return PlatformBehavior.TYPE;
    }

    @Override
    public PlatformBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return PlatformBehavior.deserialize(aObjectInstance, aRuntime);
    }
}
