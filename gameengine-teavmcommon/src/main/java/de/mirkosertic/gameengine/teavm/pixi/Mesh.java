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
import org.teavm.jso.JSProperty;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Uint16Array;

public abstract class Mesh extends Container {

    public final static int DRAW_MODE_TRIANGLE_MESH = 0;
    public final static int DRAW_MODE_TRIANGLES = 1;

    @JSBody(params = {"aTexture", "aVertices"}, script = "return new PIXI.mesh.Mesh(aTexture, aVertices);")
    public static native Mesh createMesh(Texture aTexture, Float32Array aVertices);

    @JSProperty
    public abstract void setVertices(Float32Array aVertices);

    @JSProperty
    public abstract Float32Array getVertices();

    @JSProperty
    public abstract void setUvs(Float32Array aUVs);

    @JSProperty
    public abstract void setIndices(Uint16Array aIncices);

    @JSProperty
    public abstract void setDrawMode(int aDrawMode);

}
