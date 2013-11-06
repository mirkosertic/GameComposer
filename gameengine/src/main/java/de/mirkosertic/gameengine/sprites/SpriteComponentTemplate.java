package de.mirkosertic.gameengine.sprites;

import de.mirkosertic.gameengine.core.*;

import java.util.HashMap;
import java.util.Map;

public class SpriteComponentTemplate implements GameComponentTemplate<SpriteComponent> {

    private ResourceName resourceName;
    private GameObject owner;

    public SpriteComponentTemplate(GameObject aOwner) {
        owner = aOwner;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public SpriteComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new SpriteComponent(aInstance);
    }

    @Override
    public String getTypeKey() {
        return SpriteComponent.TYPE;
    }

    public ResourceName getResourceName() {
        return resourceName;
    }

    public void setResourceName(ResourceName resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(SpriteComponent.TYPE_ATTRIBUTE, SpriteComponent.TYPE);        
        theResult.put("resourcename", getResourceName().serialize());
        return theResult;
    }

    public static SpriteComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        SpriteComponentTemplate theResult = new SpriteComponentTemplate(aOwner);
        theResult.setResourceName(ResourceName.deserialize((Map<String, Object>) aSerializedData.get("resourcename")));
        return theResult;
    }
}