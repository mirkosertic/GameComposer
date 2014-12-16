package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class SpriteBehavior implements Behavior, Sprite, Reflectable<SpriteClassInformation> {

    static final String TYPE = "Sprite";

    private final GameObjectInstance objectInstance;
    private final Property<ResourceName> resourceName;

    private SpriteBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getBehaviorTemplate(SpriteBehaviorTemplate.class));
    }

    SpriteBehavior(GameObjectInstance aObjectInstance, SpriteBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        resourceName = new Property<>(ResourceName.class, this, RESOURCE_NAME_PROPERTY, aTemplate.resourceNameProperty().get(), theEventManager);
    }

    @Override
    public SpriteClassInformation getClassInformation() {
        return SpriteClassInformation.INSTANCE;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    public Property<ResourceName> resourceNameProperty() {
        return resourceName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public SpriteBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(SpriteBehaviorTemplate.class);
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    public static SpriteBehavior deserialize(GameObjectInstance aObjectInstance) {
        return new SpriteBehavior(aObjectInstance);
    }
}