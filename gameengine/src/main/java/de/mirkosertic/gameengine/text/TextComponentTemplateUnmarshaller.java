package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class TextComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<TextComponentTemplate> {

    @Override
    public String getTypeKey() {
        return TextComponent.TYPE;
    }

    @Override
    public TextComponentTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return TextComponentTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
