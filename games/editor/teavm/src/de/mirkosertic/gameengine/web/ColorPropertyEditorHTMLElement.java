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
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.type.Color;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class ColorPropertyEditorHTMLElement implements HTMLElement {

    @JSBody(params = {}, script = "return document.createElement('gameeditor-colorpropertyeditor');")
    public static native ColorPropertyEditorHTMLElement create();

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    @JSProperty
    public abstract void setValue(String aValue);

    @JSProperty
    public abstract String getValue();

    public HTMLInputBinder bindTo(Property<Color> aProperty) {
        setValue(CSSUtils.toColorHex(aProperty.get()));
        addEventListener("value-changed", aEvent -> aProperty.set(CSSUtils.fromColorHex(getValue())));
        GameEventListener<PropertyChanged> theListener = aEvent -> setValue(CSSUtils.toColorHex(aProperty.get()));
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}
