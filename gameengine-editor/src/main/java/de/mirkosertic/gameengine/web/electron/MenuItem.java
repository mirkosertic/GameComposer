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
package de.mirkosertic.gameengine.web.electron;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public abstract class MenuItem implements JSObject {

    @JSFunctor
    public static interface ActionListener extends JSObject {

        void run();
    }

    @JSBody(params = {"aScope", "aLabel", "aActionListener"}, script = "return new aScope.MenuItem({label: aLabel, click: aActionListener});")
    public static native MenuItem createMenuItem(Scope aScope, String aLabel, ActionListener aActionListener);

    @JSBody(params = {"aScope"}, script = "return new aScope.MenuItem({type: 'separator'});")
    public static native MenuItem createReparator(Scope aScope);

    @JSBody(params = {"aScope", "aLabel", "aSubmenu"}, script = "return new aScope.MenuItem({label: aLabel, submenu: aSubmenu});")
    public static native MenuItem createMenuItem(Scope aScope, String aLabel, Menu aSubmenu);
}
