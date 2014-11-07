package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;

public class DragomeSound {

    private final Object audioObject;

    public DragomeSound(Object aAudioObject) {
        audioObject = aAudioObject;
    }

    public void play() {
        DragomeLogger.info("Playing " + audioObject);
        ScriptHelper.put("s", audioObject, this);
        ScriptHelper.evalNoResult("s.play()", this);
    }

    public void stop() {
        ScriptHelper.put("s", audioObject, this);
        ScriptHelper.evalNoResult("s.stop()", this);
    }
}
