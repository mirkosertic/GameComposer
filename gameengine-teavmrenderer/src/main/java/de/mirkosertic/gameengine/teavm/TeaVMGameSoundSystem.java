package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

import org.teavm.jso.JS;
import org.teavm.jso.JSConstructor;
import org.teavm.jso.JSObject;

class TeaVMGameSoundSystem implements GameSoundSystem<TeaVMSound> {

    private interface AudioFactory extends JSObject {

        @JSConstructor(value = "Audio")
        TeaVMSound create(String aResourceName);
    }

    private final GameRuntime runtime;

    public TeaVMGameSoundSystem(GameRuntime aRuntime) {
        runtime = aRuntime;
    }

    @Override
    public TeaVMSound play(ResourceName aResourceName) {
        try {
            TeaVMGameResource theResource = runtime.getResourceCache().getResourceFor(aResourceName);
            if (theResource != null && theResource.getType() == GameResourceType.SOUND) {
                TeaVMSound theSound = ((AudioFactory) JS.getGlobal()).create(theResource.getName());
                theSound.play();
                return theSound;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void stop(TeaVMSound aSoundObject) {
    }
}