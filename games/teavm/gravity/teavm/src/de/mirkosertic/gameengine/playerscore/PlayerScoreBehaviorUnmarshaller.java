package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class PlayerScoreBehaviorUnmarshaller implements BehaviorUnmarshaller<PlayerScoreBehavior> {

    @Override
    public String getTypeKey() {
        return PlayerScoreBehavior.TYPE;
    }

    @Override
    public PlayerScoreBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return PlayerScoreBehavior.deserialize(aObjectInstance, aSerializedData);
    }
}
