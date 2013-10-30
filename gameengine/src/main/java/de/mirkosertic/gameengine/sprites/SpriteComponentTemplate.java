package de.mirkosertic.gameengine.sprites;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class SpriteComponentTemplate extends SpriteData implements GameComponentTemplate<SpriteComponent> {

    public SpriteComponentTemplate() {
    }

    public SpriteComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new SpriteComponent(aInstance, getResourceName());
    }

    @Override
    public String getTypeKey() {
        return SpriteComponent.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(SpriteComponent.TYPE_ATTRIBUTE, SpriteComponent.TYPE);        
        theResult.put("resourcename", getResourceName().serialize());
        return theResult;
    }

    public static SpriteComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        SpriteComponentTemplate theResult = new SpriteComponentTemplate();
        theResult.setResourceName(ResourceName.deserialize((Map<String, Object>) aSerializedData.get("resourcename")));
        return theResult;
    }
}