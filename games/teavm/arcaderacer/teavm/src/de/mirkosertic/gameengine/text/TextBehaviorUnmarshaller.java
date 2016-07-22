package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class TextBehaviorUnmarshaller implements BehaviorUnmarshaller<TextBehavior> {

    @Override
    public String getTypeKey() {
        return TextBehavior.TYPE;
    }

    @Override
    public TextBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return TextBehavior.deserialize(aObjectInstance, aSerializedData);
    }
}
