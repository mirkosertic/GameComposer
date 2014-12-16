package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class SpriteBehaviorTemplate implements BehaviorTemplate<SpriteBehavior>, Sprite, Reflectable<SpriteClassInformation> {

    private final Property<ResourceName> resourceName;
    private final GameObject owner;

    @UsedByReflection
    public SpriteBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        resourceName = new Property<>(ResourceName.class, this, RESOURCE_NAME_PROPERTY, aEventManager);
        owner = aOwner;
    }

    @Override
    public SpriteClassInformation getClassInformation() {
        return SpriteClassInformation.INSTANCE;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public SpriteBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new SpriteBehavior(aInstance, this);
    }

    @Override
    public String getTypeKey() {
        return SpriteBehavior.TYPE;
    }

    public Property<ResourceName> resourceNameProperty() {
        return resourceName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(SpriteBehavior.TYPE_ATTRIBUTE, SpriteBehavior.TYPE);
        if (resourceName.get() != null) {
            theResult.put("resourcename", resourceName.get().serialize());
        }
        return theResult;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }

    public static SpriteBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        SpriteBehaviorTemplate theResult = new SpriteBehaviorTemplate(aEventManager, aOwner);
        Map<String, Object> theResourceName = (Map<String, Object>) aSerializedData.get("resourcename");
        if (theResourceName != null) {
            theResult.resourceName.setQuietly(ResourceName.deserialize(theResourceName));
        }
        return theResult;
    }
}
