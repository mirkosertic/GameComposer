package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Uint16Array;

public abstract class Mesh extends Container {

    public final static int DRAW_MODE_TRIANGLE_MESH = 0;
    public final static int DRAW_MODE_TRIANGLES = 1;

    @JSBody(params = {"aTexture"}, script = "return new PIXI.mesh.Mesh(aTexture);")
    public static native Mesh createMesh(Texture aTexture);

    @JSProperty
    public abstract void setVertices(Float32Array aVertices);

    @JSProperty
    public abstract void setUvs(Float32Array aUVs);

    @JSProperty
    public abstract void setIndices(Uint16Array aIndives);

    @JSProperty
    public abstract void setDrawMode(int aDrawMode);

}
