package de.mirkosertic.gameengine.action;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.sound.PlaySound;

public class PlaySoundAction implements Action {

    public static final String TYPE_VALUE = "PlaySoundAction";

    private final Property<ResourceName> resourceName;

    public PlaySoundAction() {
        resourceName = new Property<ResourceName>(this, "resourceName", (ResourceName) null);
    }

    @Override
    public void invoke(GameScene aScene) {
        GameEventManager theManager = aScene.getRuntime().getEventManager();
        theManager.fire(new PlaySound(resourceName.get()));
    }

    public Property<ResourceName> resourceNameProperty() {
        return resourceName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        if (resourceName.get() != null) {
            theResult.put("resourceName", resourceName.get().name);
        }
        return theResult;
    }

    public static PlaySoundAction unmarshall(Map<String, Object> aSerializedData) {
        PlaySoundAction theResult = new PlaySoundAction();
        String theResourceName = (String) aSerializedData.get("resourceName");
        if (theResourceName != null) {
            theResult.resourceName.setQuietly(new ResourceName(theResourceName));
        }
        return theResult;
    }
}