package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;

public abstract class Mesh extends Container {

    @JSBody(params = {"aTexture", "aVertices"}, script = "return new PIXI.mesh.Mesh(aTexture, aVertices);")
    public static native Mesh createMesh(Texture aTexture, float[] aVertices);

}
