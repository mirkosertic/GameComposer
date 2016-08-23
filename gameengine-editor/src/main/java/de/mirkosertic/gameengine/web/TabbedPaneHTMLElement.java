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

import de.mirkosertic.gameengine.type.UUID;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.Map;

public abstract class TabbedPaneHTMLElement implements HTMLElement {

    public static class Manager {

        private final TabbedPaneHTMLElement element;
        private final Map<Object, Integer> knownObjects;

        public Manager(TabbedPaneHTMLElement aElement) {
            element = aElement;
            knownObjects = new HashMap<>();
        }

        public void addTab(String aTitle, HTMLElement aElement, Object aOwner) {
            if (knownObjects.containsKey(aOwner)) {
                element.selectTab(knownObjects.get(aOwner));
            } else {
                knownObjects.put(aOwner, element.addTab(aTitle, aElement));
            }
        }

        public void clearAll() {
            element.clearAll();
        }
    }

    public abstract void clearAll();

    public abstract int addTab(String aTitle, HTMLElement aElement);

    public abstract void selectTab(int aIndex);
}