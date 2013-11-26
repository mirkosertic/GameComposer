package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class ConstantMovementUnmarshaller implements GameComponentUnmarshaller<ConstantMovement> {

    @Override
    public String getTypeKey() {
        return ConstantMovement.TYPE;
    }

    @Override
    public ConstantMovement deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return ConstantMovement.deserialize(aRuntime, aObjectInstance, aSerializedData);
    }
}
