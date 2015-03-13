package de.mirkosertic.gameengine.physic;

import java.util.Map;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class StaticBehaviorUnmarshaller implements BehaviorUnmarshaller<StaticBehavior> {

    @Override
    public String getTypeKey() {
        return StaticBehavior.TYPE;
    }

    @Override
    public StaticBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return StaticBehavior.deserialize(aObjectInstance);
    }
}
