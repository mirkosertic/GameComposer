package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSObject;

public interface Renderer extends JSObject {

    void render(Stage aStage);
}
