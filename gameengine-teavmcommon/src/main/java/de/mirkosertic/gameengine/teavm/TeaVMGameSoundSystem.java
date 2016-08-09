package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.teavm.howler.Config;
import de.mirkosertic.gameengine.teavm.howler.Howl;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

class TeaVMGameSoundSystem implements GameSoundSystem<TeaVMSound> {

    private final GameRuntime runtime;
    private final Map<String, Howl> howls;

    public TeaVMGameSoundSystem(GameRuntime aRuntime) {
        runtime = aRuntime;
        howls = new HashMap<>();
    }

    @Override
    public TeaVMSound play(ResourceName aResourceName) {
        try {
            TeaVMSoundResource theResource = runtime.getResourceCache().getResourceFor(aResourceName);
            if (theResource != null && theResource.getType() == GameResourceType.SOUND) {

                Howl theHowl = howls.get(theResource.getName());
                if (theHowl == null) {
                    Config theConfig = Config.createConfig();
                    theConfig.setSrc(new String[] {theResource.getName()});
                    theHowl = Howl.createHowl(theConfig);
                    howls.put(theResource.getName(), theHowl);
                }

                return new TeaVMSound(theHowl, theHowl.play());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void stop(TeaVMSound aSoundObject) {
        aSoundObject.howl.stop(aSoundObject.soundID);
    }
}