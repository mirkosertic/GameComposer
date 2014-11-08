package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

import com.dragome.commons.javascript.ScriptHelper;

public class DragomeGameSoundSystem implements GameSoundSystem<DragomeSound> {

    private final GameResourceCache resourceCache;

    public DragomeGameSoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
    }

    @Override
    public DragomeSound play(ResourceName aResourceName) {
        try {
            GameResource theResource = resourceCache.getResourceFor(aResourceName);
            if (theResource != null && theResource.getType() == GameResourceType.SOUND) {
                DragomeGameResource theGameResource = (DragomeGameResource) theResource;
                ScriptHelper.put("sn", theGameResource.getName(), this);
                ScriptHelper.evalNoResult("new Audio(sn).play()", this);
                return new DragomeSound();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void stop(DragomeSound aSoundObject) {
    }
}