package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.teavm.howler.Howl;
import de.mirkosertic.gameengine.teavm.howler.SoundID;

public class TeaVMSound {

    public final Howl howl;
    public final SoundID soundID;

    public TeaVMSound(Howl aHowl, SoundID aSoundID) {
        howl = aHowl;
        soundID = aSoundID;
    }
}
