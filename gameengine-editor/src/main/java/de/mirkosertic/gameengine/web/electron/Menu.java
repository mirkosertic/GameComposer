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
import org.teavm.jso.JSObject;

public abstract class Menu implements JSObject {

    @JSBody(params = {"aScope"}, script = "return new aScope.Menu();")
    public static native Menu createMenu(Scope aScope);

    @JSBody(params = {"aScope", "aMenu"}, script = "aScope.Menu.setApplicationMenu(aMenu);")
    public static native void setApplicationMenu(Scope aScope, Menu aMenu);

    public abstract void append(MenuItem aItem);
}
