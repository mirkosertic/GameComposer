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
import de.mirkosertic.gameengine.type.Font;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLOptionElement;

public abstract class FontPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel1(String aLabel);

    @JSProperty
    public abstract String getLabel1();

    @JSProperty
    public abstract void setLabel2(String aLabel);

    @JSProperty
    public abstract String getLabel2();

    @JSProperty
    public abstract void setIndex(int aValue);

    @JSProperty
    public abstract int getIndex();

    @JSProperty
    public abstract void setSize(String aSize);

    @JSProperty
    public abstract String getSize();

    @JSBody(params = {"aElement"}, script = "Polymer.dom(this).appendChild(aElement);")
    public abstract void addOption(HTMLOptionElement aElement);

    private <T> int indexOf(T aValue, T[] aValues) {
        for (int i=0;i<aValues.length;i++) {
            T theEntry = aValues[i];
            if (theEntry == aValue) {
                return i;
            }
        }
        return -1;
    }

    public HTMLInputBinder bindTo(Property<Font> aProperty, Font.FontName[] aValues) {

        int theCurrentIndex = indexOf(aProperty.get().name, aValues);

        for (int i=0; i<aValues.length; i++) {
            Font.FontName theValue = aValues[i];
            HTMLOptionElement theOption = (HTMLOptionElement) getOwnerDocument().createElement("option");
            theOption.setInnerHTML(theValue.toString());
            addOption(theOption);
        }

        setIndex(theCurrentIndex);
        setSize(Integer.toString(aProperty.get().size));

        addEventListener("index-changed", aEvent -> {
            aProperty.set(aProperty.get().changeName(aValues[getIndex()]));
        });
        addEventListener("size-changed", aEvent -> {
            aProperty.set(aProperty.get().changeSize(Integer.parseInt(getSize())));
        });
        GameEventListener<PropertyChanged> theListener = aEvent -> {
            Font theFont = aProperty.get();
            setIndex(indexOf(theFont.name, aValues));
            setSize(Integer.toString(theFont.size));
        };
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}