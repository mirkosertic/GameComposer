package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.model.EventDispatcherImpl;
import com.dragome.services.ServiceLocator;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;

public final class Window {

    public Window() {
    }

    public int getClientWidth() {
        return ScriptHelper.evalInt("window.innerWidth", this);
    }

    public int getClientHeight() {
        return ScriptHelper.evalInt("window.innerHeight", this);
    }

    public void requestAnimationFrame(Runnable aRunnable) {
        ScriptHelper.put("ra", aRunnable, this);
        ScriptHelper.evalNoResult("requestAnimationFrame(function(time) {ra.$run$void();})", this);
    }

    public void addEventListener(EventListener aEventListener, String... aEvent) {
        Element theElement = ServiceLocator.getInstance().getDomHandler().getElementBySelector("body");
        EventDispatcherImpl.setEventListener(theElement, aEventListener, aEvent);
    }

    public void onResize(Runnable aRunnable) {
        ScriptHelper.put("ra", aRunnable, this);
        ScriptHelper.evalNoResult("window.addEventListener('resize', function() {ra.$run$void();})", this);
    }
}