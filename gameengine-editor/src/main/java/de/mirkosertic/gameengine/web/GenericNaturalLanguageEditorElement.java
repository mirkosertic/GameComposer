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

import de.mirkosertic.gameengine.event.Property;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.dom.html.HTMLLinkElement;
import org.teavm.jso.dom.html.HTMLSelectElement;
import org.teavm.jso.dom.xml.Document;

public abstract class GenericNaturalLanguageEditorElement implements HTMLElement {

    private static final Document DOCUMENT = Window.current().getDocument();

    @JSBody(params = {}, script = "return document.createElement('p');")
    public static native GenericNaturalLanguageEditorElement create();

    public HTMLImageElement addDeleteImage() {
        HTMLImageElement theElement = (HTMLImageElement) DOCUMENT.createElement("div");
        theElement.setAttribute("class", "genericdeletebutton");
        appendChild(theElement);
        return theElement;
    }

    public void addText(String aText) {
        appendChild(DOCUMENT.createTextNode(aText));
    }

    public <T> void addInput(Property<T> aProperty, ObjectToStringConverter<T> aConverter) {
        HTMLSelectElement theInputElement = (HTMLSelectElement) DOCUMENT.createElement("input");
        theInputElement.setValue(aConverter.convertFrom(aProperty.get()));
        appendChild(theInputElement);
    }

    public <T> void addSelection(Property<T> aProperty, T[] aAllowedValues, ObjectToStringConverter<T> aConverter) {
        HTMLSelectElement theSelectElement = (HTMLSelectElement) DOCUMENT.createElement("select");
        T theCurrentValue = aProperty.get();
        for (T theValue : aAllowedValues) {
            EditorHTMLOptionElement theOption = EditorHTMLOptionElement.create(aConverter.convertFrom(theValue));
            if (theCurrentValue == theValue || theValue.equals(theCurrentValue)) {
                theOption.setSelected(true);
            }

            Polymer.dom(theSelectElement).appendChild(theOption);
        }
        theSelectElement.addEventListener("change", aEvent -> {
            if (theSelectElement.getSelectedIndex() >= 0) {
                aProperty.set(aAllowedValues[theSelectElement.getSelectedIndex()]);
            }
        });
        appendChild(theSelectElement);
    }

    public void addAction(String aText, EventListener<Event> aEventListener) {
        HTMLLinkElement theElement = (HTMLLinkElement)DOCUMENT.createElement("a");
        theElement.addEventListener("click", aEventListener);
        theElement.appendChild(DOCUMENT.createTextNode(aText));
        appendChild(theElement);
    }
}