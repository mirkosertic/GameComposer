package de.mirkosertic.gameengine.sprites;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class SpriteComponentUnmarshaller implements GameComponentUnmarshaller<SpriteComponent> {

    @Override
    public String getTypeKey() {
        return SpriteComponent.TYPE;
    }

    @Override
    public SpriteComponent deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        SpriteComponent theResult = SpriteComponent.deserialize(aObjectInstance, aSerializedData);
        return theResult;
    }
}
