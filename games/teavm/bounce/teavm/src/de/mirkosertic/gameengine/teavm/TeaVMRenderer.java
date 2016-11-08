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
package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

public class TeaVMRenderer {

    private static final Window window = Window.current();
    private static final HTMLDocument document = window.getDocument();

    public static void main(String[] args) {
        HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) document.getElementById("html5canvas");
        TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer();
        thePlayer.boot(theCanvasElement);

    }
}