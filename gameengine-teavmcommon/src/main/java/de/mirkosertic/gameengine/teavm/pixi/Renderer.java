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
package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public abstract class Renderer implements JSObject {

    public final static int TYPE_UNKNOWN = 0;
    public final static int TYPE_WEBGL = 1;
    public final static int TYPE_CANVAS = 2;

    @JSBody(params = { "aWidth", "aHeight", "aCanvasElement" }, script = "return PIXI.autoDetectRenderer(\n" +
            "    aWidth,\n" +
            "    aHeight," +
            "    {" +
            "       view: aCanvasElement" +
            "    } " +
            "  );")
    public static native Renderer autodetectRenderer(int aWidth, int aHeight, HTMLCanvasElement aCanvasElement);

    @JSBody(params = { "aWidth", "aHeight", "aCanvasElement" }, script = "return new PIXI.CanvasRenderer(\n" +
            "    aWidth,\n" +
            "    aHeight," +
            "    {" +
            "       view: aCanvasElement" +
            "    } " +
            "  );")
    public static native Renderer canvasRenderer(int aWidth, int aHeight, HTMLCanvasElement aCanvasElement);

    public abstract void render(Container aStage);

    @JSProperty
    public abstract void setBackgroundColor(int aColor);

    @JSProperty
    public abstract int getType();

    public abstract void resize(int aWidth, int aHeight);

}
