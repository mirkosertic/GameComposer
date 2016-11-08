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

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.ArrayList;
import java.util.List;

public abstract class TabbedPaneHTMLElement implements HTMLElement {

    public interface TabEvent extends Event {

        @JSProperty
        TabEventDetail getDetail();
    }

    public interface TabEventDetail extends JSObject {

        @JSProperty
        int getTabindex();
    }

    public interface TabHandler {

        HTMLElement getElement();

        Object getOwner();

        void handleClosed();

        void handleResize();
    }

    public static class Manager {

        private final TabbedPaneHTMLElement element;
        private final List<TabHandler> knownObjects;

        public Manager(TabbedPaneHTMLElement aElement) {
            element = aElement;
            knownObjects = new ArrayList<>();
            aElement.addEventListener("tabclose", new EventListener<TabEvent>() {
                @Override
                public void handleEvent(TabEvent aEvent) {
                    removeTab(aEvent.getDetail().getTabindex());
                }
            });
        }

        public void removeTab(int aIndex) {
            element.removeTab(aIndex);

            TabHandler theHandler = knownObjects.get(aIndex);
            theHandler.handleClosed();

            knownObjects.remove(aIndex);
        }

        public void addTab(String aTitle, TabHandler aTabHandler) {
            for (int i=0;i<knownObjects.size();i++) {
                TabHandler theHandler = knownObjects.get(i);
                if (theHandler.getOwner() == aTabHandler.getOwner()) {
                    element.selectTab(i);
                    return;
                }
            }

            element.addTab(aTitle, aTabHandler.getElement());
            knownObjects.add(aTabHandler);
        }

        public void clearAll() {
            for (TabHandler theHandler : knownObjects) {
                theHandler.handleClosed();
            }
            knownObjects.clear();
            element.clearAll();
            element.clearContent();
        }

        public void notifyResize() {
            for (TabHandler aHandler : knownObjects) {
                aHandler.handleResize();
            }
        }

        public boolean ensureSelected(Object aObject) {
            for (int i=0;i<knownObjects.size();i++) {
                TabHandler theHandler = knownObjects.get(i);
                if (theHandler.getOwner() == aObject) {
                    element.selectTab(i);
                    return true;
                }
            }
            return false;
        }
    }

    public abstract void clearAll();

    public abstract void clearContent();

    public abstract int addTab(String aTitle, HTMLElement aElement);

    public abstract void selectTab(int aIndex);

    public abstract void removeTab(int aIndex);
}