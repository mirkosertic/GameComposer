/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.teavm.TeaVMMouseEvent;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class ContextMenuHTMLElement implements HTMLElement {

    @JSBody(params = {}, script = "return document.createElement('gameeditor-contextmenu');")
    public static native ContextMenuHTMLElement create();

    @JSProperty
    public abstract void setTitle(String aTitle);

    @JSProperty
    public abstract String getTitle();

    public void add(ContextMenuItemHTMLElement aItem) {
        Polymer.dom(this).appendChild(aItem);
    }

    public void showAt(TeaVMMouseEvent aMouseEvent) {
        getStyle().setProperty("left", aMouseEvent.getClientX() - 20+"");
        getStyle().setProperty("top", aMouseEvent.getClientY() - 20+"");
        Window theWindow = Window.current();
        Polymer.dom(theWindow.getDocument().getBody()).appendChild(this);
        addEventListener("mouseleave", e -> {
            Polymer.dom(theWindow.getDocument().getBody()).removeChild(ContextMenuHTMLElement.this);
        }, false);
    }

    public void hide() {
        Polymer.dom(Window.current().getDocument().getBody()).removeChild(this);
    }
}