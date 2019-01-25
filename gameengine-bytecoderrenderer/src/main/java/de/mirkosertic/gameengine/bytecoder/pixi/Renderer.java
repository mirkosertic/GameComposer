/*
 * Copyright 2019 Mirko Sertic
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
package de.mirkosertic.gameengine.bytecoder.pixi;

import de.mirkosertic.bytecoder.api.OpaqueProperty;
import de.mirkosertic.bytecoder.api.OpaqueReferenceType;
import de.mirkosertic.bytecoder.api.web.HTMLCanvasElement;

public abstract class Renderer implements OpaqueReferenceType {

    public final static int TYPE_UNKNOWN = 0;
    public final static int TYPE_WEBGL = 1;
    public final static int TYPE_CANVAS = 2;

    public static native Renderer autodetectRenderer(int aWidth, int aHeight, HTMLCanvasElement aCanvasElement);

    public abstract void render(Container aStage);

    @OpaqueProperty
    public abstract void backgroundColor(int aColor);

    @OpaqueProperty
    public abstract int rendererType();

    public abstract void resize(int aWidth, int aHeight);
}
