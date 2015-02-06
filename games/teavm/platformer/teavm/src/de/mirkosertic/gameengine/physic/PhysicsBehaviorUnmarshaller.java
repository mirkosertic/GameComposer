package de.mirkosertic.gameengine.physic;

import java.util.Map;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class PhysicsBehaviorUnmarshaller implements BehaviorUnmarshaller<PhysicsBehavior> {

    @Override
    public String getTypeKey() {
        return PhysicsBehavior.TYPE;
    }

    @Override
    public PhysicsBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return PhysicsBehavior.deserialize(aObjectInstance, aSerializedData);
    }
}
