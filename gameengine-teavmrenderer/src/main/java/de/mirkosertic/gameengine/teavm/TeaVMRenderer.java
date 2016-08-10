package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

public class TeaVMRenderer {

    private static final Window window = Window.current();
    private static final HTMLDocument document = window.getDocument();

    public static void main(String[] args) {
        HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) document.getElementById("html5canvas");
        TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer();
        thePlayer.boot(theCanvasElement);

    }
}