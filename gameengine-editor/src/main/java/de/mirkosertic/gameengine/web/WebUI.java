package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.teavm.TeaVMGenericPlayer;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class WebUI {

    private static final Window window = Window.current();

    public static void main(String[] args) {
        if (window.getLocation().getPathName().endsWith("/index.html")) {
            Editor theEditor = new Editor();
            theEditor.boot();
        } else {
            HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) window.getDocument().getElementById("html5canvas");
            TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer();
            thePlayer.boot(theCanvasElement);
        }
    }
}