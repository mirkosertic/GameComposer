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

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.teavm.pixi.Graphics;
import de.mirkosertic.gameengine.teavm.pixi.Mesh;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.teavm.pixi.Texture;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Uint16Array;

import java.util.HashMap;
import java.util.Map;

public class TeaVMEffectCanvas implements EffectCanvas {

    private final TeaVMInstanceCache instanceCache;
    private final Renderer renderer;
    private final Map<Color, Texture> coloredTextures;

    public TeaVMEffectCanvas(TeaVMInstanceCache aInstanceCacne, Renderer aRenderer) {
        instanceCache = aInstanceCacne;
        renderer = aRenderer;
        coloredTextures = new HashMap<>();
    }

    private Texture getTexttureFor(Color aColor) {
        Texture theResult = coloredTextures.get(aColor);
        if (theResult == null) {
            Graphics theGrapics = Graphics.createGraphics();
            theGrapics.setWidth(5);
            theGrapics.setHeight(5);
            theGrapics.lineStyle(5, CSSUtils.toInt(aColor), 1);
            theGrapics.drawRect(0,0,5,5);
            theResult = theGrapics.generateTexture(1, 0);
            coloredTextures.put(aColor, theResult);
        }
        return theResult;
    }

    @Override
    public void drawSingleDot(String aObjectID, Position aPosition, final Color aColor, int aZIndex) {
        Graphics theCurrentObject = instanceCache.getOrCreate(aObjectID, new TeaVMInstanceCache.Producer<Graphics>() {
            @Override
            public Graphics create() {
                Graphics theCurrentObject = Graphics.createGraphics();
                theCurrentObject.getScale().set(1, 1);
                theCurrentObject.setWidth(1);
                theCurrentObject.setHeight(1);
                theCurrentObject.lineStyle(1, CSSUtils.toInt(aColor), 1f);
                theCurrentObject.drawRect(0, 0, 1, 1);
                return theCurrentObject;
            }
        }, 0);

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPosition.x, aPosition.y);

    }

    @Override
    public void fillRectangle(String aObjectID, final int aX0, final int aY0, final int aX1, final int aY1, final int aX2, final int aY2, final int aX3, final int aY3, final Color aColor, int aZIndex) {

        Mesh theMesh = instanceCache.getOrCreate(aObjectID, new TeaVMInstanceCache.Producer<Mesh>() {
            @Override
            public Mesh create() {
                Mesh theMesh = Mesh.createMesh(getTexttureFor(aColor), Float32Array.create(8));
                if (renderer.getType() == Renderer.TYPE_CANVAS) {
                    theMesh.setDrawMode(Mesh.DRAW_MODE_TRIANGLES);
                    Uint16Array theIndices = Uint16Array.create(6);
                    theIndices.set(0, 0);
                    theIndices.set(1, 1);
                    theIndices.set(2, 2);
                    theIndices.set(3, 0);
                    theIndices.set(4, 2);
                    theIndices.set(5, 3);
                    theMesh.setIndices(theIndices);
                }
                return theMesh;
            }
        }, aZIndex);

        Float32Array theVertices = theMesh.getVertices();
        theVertices.set(0, aX0);
        theVertices.set(1, aY0);
        theVertices.set(2, aX1);
        theVertices.set(3, aY1);
        theVertices.set(4, aX2);
        theVertices.set(5, aY2);
        theVertices.set(6, aX3);
        theVertices.set(7, aY3);
    }

    @Override
    public void fillRectangle(String aObjectIdentifier, final GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aX3, int aY3, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2, double aU3, double aV3, int aZIndex) {
        Mesh theMesh = instanceCache.getOrCreate(aObjectIdentifier, new TeaVMInstanceCache.Producer<Mesh>() {
            @Override
            public Mesh create() {
                TeaVMTextureResource theResource = (TeaVMTextureResource) aTexture;
                Mesh theMesh =  Mesh.createMesh(theResource.getTexture(), Float32Array.create(8));

                if (renderer.getType() == Renderer.TYPE_CANVAS) {
                    theMesh.setDrawMode(Mesh.DRAW_MODE_TRIANGLES);
                    Uint16Array theIndices = Uint16Array.create(6);
                    theIndices.set(0, 0);
                    theIndices.set(1, 1);
                    theIndices.set(2, 2);
                    theIndices.set(3, 0);
                    theIndices.set(4, 2);
                    theIndices.set(5, 3);
                    theMesh.setIndices(theIndices);
                }
                return theMesh;
            }
        }, aZIndex);

        Float32Array theVertices = theMesh.getVertices();
        theVertices.set(0, aX0);
        theVertices.set(1, aY0);
        theVertices.set(2, aX1);
        theVertices.set(3, aY1);
        theVertices.set(4, aX2);
        theVertices.set(5, aY2);
        theVertices.set(6, aX3);
        theVertices.set(7, aY3);
    }
}