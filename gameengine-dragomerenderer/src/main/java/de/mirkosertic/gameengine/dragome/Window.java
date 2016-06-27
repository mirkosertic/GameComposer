package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.services.WebServiceLocator;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

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
        EventTarget theElement = (EventTarget) WebServiceLocator.getInstance().getDomHandler().getElementBySelector("body");
        for (String theEvent : aEvent) {
            theElement.addEventListener(theEvent, aEventListener, true);
        }
    }

    public void onResize(Runnable aRunnable) {
	    ScriptHelper.put("ra", aRunnable, this);
        ScriptHelper.evalNoResult("window.addEventListener('resize', function() {ra.$run$void();})", this);
    }
}
