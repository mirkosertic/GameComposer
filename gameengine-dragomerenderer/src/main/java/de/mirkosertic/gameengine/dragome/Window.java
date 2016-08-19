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
package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.services.WebServiceLocator;
import com.dragome.web.enhancers.jsdelegate.JsCast;

import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public final class Window {

    public Window() {
    }

    public int getClientWidth() {
        return ScriptHelper.evalInt("window.innerWidth", this);
    }

    public int getClientHeight() {
        return ScriptHelper.evalInt("window.innerHeight", this);
    }

    public void requestAnimationFrame(Runnable aRunnable) {
	    ScriptHelper.put("ra", aRunnable, this);
        ScriptHelper.evalNoResult("requestAnimationFrame(function(time) {ra.$run$void();})", this);
    }

    public void addEventListener(EventListener aEventListener, String... aEvent) {
        EventTarget theElement = JsCast.castTo(WebServiceLocator.getInstance().getDomHandler().getElementBySelector("body"), EventTarget.class);
        for (String theEvent : aEvent) {
            theElement.addEventListener(theEvent, aEventListener, true);
        }
    }

    public void onResize(Runnable aRunnable) {
	    ScriptHelper.put("ra", aRunnable, this);
        ScriptHelper.evalNoResult("window.addEventListener('resize', function() {ra.$run$void();})", this);
    }
}
