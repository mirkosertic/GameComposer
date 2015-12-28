package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class SpriteBehaviorUnmarshaller implements BehaviorUnmarshaller<SpriteBehavior> {

    @Override
    public String getTypeKey() {
        return SpriteBehavior.TYPE;
    }

    @Override
    public SpriteBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return SpriteBehavior.deserialize(aObjectInstance);
    }
}
