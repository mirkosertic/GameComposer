package de.mirkosertic.gameengine.sound;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

public class PlaySoundAction implements Action {

    public static final String TYPE_VALUE = "PlaySoundAction";
    public static final String RESOURCE_NAME_PROPERTY = "resourceName";

    private final Property<ResourceName> resourceName;

    @UsedByReflection
    public PlaySoundAction() {
        resourceName = new Property<>(ResourceName.class, this, RESOURCE_NAME_PROPERTY, (ResourceName) null);
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        GameEventManager theManager = aScene.getRuntime().getEventManager();
        theManager.fire(new PlaySound(resourceName.get()));
    }

    public Property<ResourceName> resourceNameProperty() {
        return resourceName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        if (resourceName.get() != null) {
            theResult.put(RESOURCE_NAME_PROPERTY, resourceName.get().name);
        }
        return theResult;
    }

    public static PlaySoundAction unmarshall(Map<String, Object> aSerializedData) {
        PlaySoundAction theResult = new PlaySoundAction();
        String theResourceName = (String) aSerializedData.get(RESOURCE_NAME_PROPERTY);
        if (theResourceName != null) {
            theResult.resourceName.setQuietly(new ResourceName(theResourceName));
        }
        return theResult;
    }
}