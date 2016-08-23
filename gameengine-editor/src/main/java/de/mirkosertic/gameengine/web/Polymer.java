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

import jdk.nashorn.api.scripting.JSObject;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class Polymer implements JSObject {

    @JSBody(params = "aElement", script = "return Polymer.dom(aElement);")
    public static native PolymerDOM dom(HTMLElement aElement);
}
