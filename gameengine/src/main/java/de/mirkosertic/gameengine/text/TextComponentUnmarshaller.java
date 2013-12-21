package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.GameComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class TextComponentUnmarshaller implements GameComponentUnmarshaller<TextComponent> {

    @Override
    public String getTypeKey() {
        return TextComponent.TYPE;
    }

    @Override
    public TextComponent deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return TextComponent.deserialize(aObjectInstance, aSerializedData);
    }
}
