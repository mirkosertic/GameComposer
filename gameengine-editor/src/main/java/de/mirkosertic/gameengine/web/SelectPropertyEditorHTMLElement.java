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
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class SelectPropertyEditorHTMLElement implements HTMLElement {

    interface ToStringConverter<T> {
        String convertToString(T aValue);
    }

    private static final ToStringConverter DEFAULT_TO_STRING_CONVERTER = aValue -> aValue.toString();

    @JSBody(params = {}, script = "return document.createElement('gameeditor-selectpropertyeditor');")
    public static native SelectPropertyEditorHTMLElement create();

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    @JSProperty
    public abstract void setIndex(int aValue);

    @JSProperty
    public abstract int getIndex();

    private <T> int indexOf(T aValue, T[] aValues) {
        for (int i=0;i<aValues.length;i++) {
            T theEntry = aValues[i];
            if (theEntry == aValue || theEntry.equals(aValue)) {
                return i;
            }
        }
        return -1;
    }

    public <T> HTMLInputBinder bindTo(Property<T> aProperty, T[] aValues) {
        return this.bindTo(aProperty, aValues, DEFAULT_TO_STRING_CONVERTER);
    }

    public <T> HTMLInputBinder bindTo(Property<T> aProperty, T[] aValues, ToStringConverter<T> aConverter) {

        int theCurrentIndex = indexOf(aProperty.get(), aValues);

        for (int i=0; i<aValues.length; i++) {
            T theValue = aValues[i];

            EditorHTMLOptionElement theOption = EditorHTMLOptionElement.create(aConverter.convertToString(theValue));

            Polymer.dom(this).appendChild(theOption);
        }

        setIndex(theCurrentIndex);
        addEventListener("index-changed", aEvent -> aProperty.set(aValues[getIndex()]));
        GameEventListener<PropertyChanged> theListener = aEvent -> setIndex(indexOf(aProperty.get(), aValues));
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}