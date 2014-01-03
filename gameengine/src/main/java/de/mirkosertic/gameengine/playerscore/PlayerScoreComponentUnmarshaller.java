package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.GameComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class PlayerScoreComponentUnmarshaller implements GameComponentUnmarshaller<PlayerScoreComponent> {

    @Override
    public String getTypeKey() {
        return PlayerScoreComponent.TYPE;
    }

    @Override
    public PlayerScoreComponent deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return PlayerScoreComponent.deserialize(aObjectInstance, aSerializedData);
    }
}
