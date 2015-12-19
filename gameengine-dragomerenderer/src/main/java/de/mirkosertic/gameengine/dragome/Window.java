package de.mirkosertic.gameengine.dragome;

import com.dragome.web.html.dom.w3c.BrowserDomHandler;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.web.dispatcher.EventDispatcherImpl;

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
        Element theElement = new BrowserDomHandler().getElementBySelector("body");
        EventDispatcherImpl.setEventListener(theElement, aEventListener, aEvent);
    }

    public void onResize(Runnable aRunnable) {
	    ScriptHelper.put("ra", aRunnable, this);
        ScriptHelper.evalNoResult("window.addEventListener('resize', function() {ra.$run$void();})", this);
    }
}
