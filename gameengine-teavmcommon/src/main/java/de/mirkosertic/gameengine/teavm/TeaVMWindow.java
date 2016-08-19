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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class TeaVMWindow {

    @JSFunctor
    public interface RenderFrameHandler extends JSObject {
        void renderFrame(int aTimeDelta);
    }

    @JSBody(
            params = {"aHandler"},
            script = "window.requestAnimationFrame(aHandler);"
    )
    public native static void requestAnimationFrame(RenderFrameHandler aHandler);

    @JSBody(
            params = {"aElement"},
            script = "fullscreen(aElement);"
    )
    public native static void requestFullScreen(HTMLElement aElement);
}
