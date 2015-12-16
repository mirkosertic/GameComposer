package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public abstract class TeaVMWindow {

    @JSFunctor
    public interface RenderFrameHandler extends JSObject {
        void renderFrame(int aTimeDelta);
    }

    @JSBody(
            params = {"aHandler"},
            script = "window.requestAnimationFrame(aHandler);"
    )
    public native static void requestAnimationFrame(RenderFrameHandler aHandler);
}
