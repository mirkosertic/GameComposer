package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

class TeaVMGameSoundSystem implements GameSoundSystem<TeaVMSound> {

    private final GameRuntime runtime;

    public TeaVMGameSoundSystem(GameRuntime aRuntime) {
        runtime = aRuntime;
    }

    @Override
    public TeaVMSound play(ResourceName aResourceName) {
        try {
            TeaVMSoundResource theResource = runtime.getResourceCache().getResourceFor(aResourceName);
            if (theResource != null && theResource.getType() == GameResourceType.SOUND) {
                TeaVMSound theSound = TeaVMSound.create(theResource.getName());
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