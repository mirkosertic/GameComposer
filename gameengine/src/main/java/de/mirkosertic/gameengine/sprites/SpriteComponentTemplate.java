package de.mirkosertic.gameengine.sprites;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.ResourceName;

public class SpriteComponentTemplate implements GameComponentTemplate<SpriteComponent> {

    private ResourceName resourceName;

    public SpriteComponentTemplate() {
    }

    public ResourceName getResourceName() {
        return resourceName;
    }

    public void setResourceName(ResourceName resourceName) {
        this.resourceName = resourceName;
    }

    public SpriteComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new SpriteComponent(aInstance, resourceName);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(SpriteComponent.TYPE_ATTRIBUTE, SpriteComponent.TYPE);        
        theResult.put("resourcename", resourceName.serialize());
        return theResult;
    }

    public static SpriteComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        SpriteComponentTemplate theResult = new SpriteComponentTemplate();
        theResult.setResourceName(ResourceName.deserialize((Map<String, Object>) aSerializedData.get("resourcename")));
        return theResult;
    }
}