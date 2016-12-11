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

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.teavm.TeaVMMouseEvent;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class TreeItemHTMLElement implements HTMLElement {

    @JSBody(params = {}, script = "return document.createElement('gameeditor-treeitem');")
    public static native TreeItemHTMLElement create();

    @JSProperty
    public abstract void setText(String aLabel);

    @JSProperty
    public abstract String getText();

    @JSProperty
    public abstract void setLevel(int aValue);

    @JSProperty
    public abstract int getLevel();

    @JSProperty
    public abstract void setSelected(boolean aValue);

    @JSProperty
    public abstract boolean isSelected();

    @JSProperty
    public abstract void setDraggable(boolean aValue);

    @JSProperty
    public abstract boolean isDraggable();

    @JSProperty
    public abstract void setSeparator(boolean aValue);

    @JSProperty
    public abstract boolean isSeparator();

    public abstract void scrollIntoView(boolean aParam);

    public abstract void addDeleteListener(EventListener<TeaVMMouseEvent> aListener);

    public abstract void addContextMenuListener(EventListener<TeaVMMouseEvent> aListener);

    public HTMLInputBinder bindTo(Property<String> aProperty) {
        setText(aProperty.get());
        GameEventListener<PropertyChanged> theListener = aEvent -> setText(aProperty.get());
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}
