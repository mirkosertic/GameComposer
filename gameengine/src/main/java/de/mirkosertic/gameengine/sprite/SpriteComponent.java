package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class SpriteComponent extends GameComponent implements Sprite, Reflectable<SpriteClassInformation> {

    static final String TYPE = "SpriteComponent";

    private final GameObjectInstance objectInstance;
    private final Property<ResourceName> resourceName;

    private SpriteComponent(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(SpriteComponentTemplate.class));
    }

    SpriteComponent(GameObjectInstance aObjectInstance, SpriteComponentTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        resourceName = new Property<ResourceName>(ResourceName.class, this, RESOURCE_NAME_PROPERTY, aTemplate.resourceNameProperty().get(), theEventManager);
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
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public SpriteComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(SpriteComponentTemplate.class);
    }

    public static SpriteComponent deserialize(GameObjectInstance aObjectInstance) {
        return new SpriteComponent(aObjectInstance);
    }
}