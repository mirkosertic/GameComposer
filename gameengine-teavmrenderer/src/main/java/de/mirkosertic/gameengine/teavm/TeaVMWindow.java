package de.mirkosertic.gameengine.teavm;

import org.teavm.dom.browser.Window;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

interface TeaVMWindow extends Window {

    @JSFunctor
    public interface RenderFrameHandler extends JSObject {
        void renderFrame(int aTimeDelta);
    }

    @JSProperty(value = "innerWidth")
    int getInnerWidth();

    @JSProperty(value = "innerHeight")
    int getInnerHeight();

    @JSProperty(value = "location")
    TeaVMWindowLocation getLocation();

    void requestAnimationFrame(RenderFrameHandler aHandler);
}
