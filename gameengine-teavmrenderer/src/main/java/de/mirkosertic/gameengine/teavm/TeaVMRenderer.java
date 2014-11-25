package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameLoopFactory;
import org.teavm.dom.browser.Window;
import org.teavm.dom.html.HTMLDocument;
import org.teavm.dom.html.HTMLElement;
import org.teavm.jso.JS;

public class TeaVMRenderer {

    private static Window window = (Window) JS.getGlobal();
    private static HTMLDocument document = window.getDocument();

    public static void main(String[] args) {

        GameLoopFactory theFactory = new GameLoopFactory();

        HTMLElement div = document.createElement("div");
        div.appendChild(document.createTextNode("TeaVM generated element" + theFactory.toString()));
        document.getBody().appendChild(div);
    }
}
