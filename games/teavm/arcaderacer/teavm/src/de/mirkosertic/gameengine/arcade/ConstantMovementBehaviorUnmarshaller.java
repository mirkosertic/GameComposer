package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class ConstantMovementBehaviorUnmarshaller implements BehaviorUnmarshaller<ConstantMovementBehavior> {

    @Override
    public String getTypeKey() {
        return ConstantMovementBehavior.TYPE;
    }

    @Override
    public ConstantMovementBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return ConstantMovementBehavior.deserialize(aRuntime, aObjectInstance, aSerializedData);
    }
}
