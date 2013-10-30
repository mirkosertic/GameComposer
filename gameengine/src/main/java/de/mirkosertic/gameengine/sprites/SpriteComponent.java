package de.mirkosertic.gameengine.sprites;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.ResourceName;

public class SpriteComponent implements GameComponent {

    public static final String TYPE = "SpriteComponent";

    private GameObjectInstance objectInstance;

    private ResourceName resourceName;

    SpriteComponent(GameObjectInstance aObjectInstance, ResourceName aResourceName) {
        objectInstance = aObjectInstance;
        resourceName = aResourceName;
    }

    public void setResourceName(ResourceName resourceName) {
        this.resourceName = resourceName;
    }

    public ResourceName getResourceName() {
        return resourceName;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        theResult.put("resourcename", resourceName.serialize());
        return theResult;
    }

    public static SpriteComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        ResourceName theResourceName = ResourceName.deserialize((Map<String, Object>) aSerializedData.get("resourcename"));
        return new SpriteComponent(aObjectInstance, theResourceName);
    }
}