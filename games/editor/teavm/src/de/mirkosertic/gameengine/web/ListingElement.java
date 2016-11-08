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

import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashSet;
import java.util.Set;

public abstract class ListingElement {

    protected final HTMLElement htmlElement;
    protected final Set<HTMLInputBinder> binder;

    public ListingElement(HTMLElement aHtmlElement) {
        htmlElement = aHtmlElement;
        binder = new HashSet<>();
    }

    protected GlobalSeparatorHTMLElement addTitleLevel1(String aTitle) {

        GlobalSeparatorHTMLElement theElement = GlobalSeparatorHTMLElement.create();
        theElement.setText(aTitle);
        theElement.setMajor(true);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    protected GlobalSeparatorHTMLElement addTitleLevel2(String aTitle) {
        GlobalSeparatorHTMLElement theElement = GlobalSeparatorHTMLElement.create();
        theElement.setText(aTitle);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    protected void clear() {
        for (HTMLInputBinder theBinder : binder) {
            theBinder.unbind();
        }
        htmlElement.clear();
    }
}