package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class TextBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<TextBehaviorTemplate> {

    @Override
    public String getTypeKey() {
        return TextBehavior.TYPE;
    }

    @Override
    public TextBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return TextBehaviorTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
