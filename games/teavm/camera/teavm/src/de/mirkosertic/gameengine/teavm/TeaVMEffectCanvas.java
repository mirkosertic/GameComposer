package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.teavm.pixi.*;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import org.teavm.jso.typedarrays.Float32Array;

import java.util.HashMap;
import java.util.Map;

public class TeaVMEffectCanvas implements EffectCanvas {

    private final InstanceCache instanceCache;
    private final Renderer renderer;
    private final Map<Color, Texture> coloredTextures;

    public TeaVMEffectCanvas(InstanceCache aInstanceCacne, Renderer aRenderer) {
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
        Graphics theCurrentObject = instanceCache.getOrCreate(aObjectID, new InstanceCache.Producer<Graphics>() {
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

        Mesh theMesh = instanceCache.getOrCreate(aObjectID, new InstanceCache.Producer<Mesh>() {
            @Override
            public Mesh create() {
                Graphics theGrapics = Graphics.createGraphics();
                theGrapics.setWidth(1);
                theGrapics.setHeight(1);
                theGrapics.lineStyle(1, CSSUtils.toInt(aColor), 1);
                theGrapics.drawRect(0,0,1,1);

                return Mesh.createMesh(getTexttureFor(aColor));
            }
        }, aZIndex);

        Float32Array theVertices = Float32Array.create(8);
        theVertices.set(0, aX0);
        theVertices.set(1, aY0);
        theVertices.set(2, aX1);
        theVertices.set(3, aY1);
        theVertices.set(4, aX2);
        theVertices.set(5, aY2);
        theVertices.set(6, aX3);
        theVertices.set(7, aY3);
        theMesh.setVertices(theVertices);
    }

    @Override
    public void fillRectangle(String aObjectID, final GameResource aTexture, final int aX0, final int aY0, final int aX1, final int aY1, final int aX2,
                              final int aY2, final int aX3, final int aY3, final int aU0, final int aV0, final int aU1, final int aV1, final int aU2, final int aV2, int aU3, int aV3, int aZIndex) {


        Mesh theMesh = instanceCache.getOrCreate(aObjectID, new InstanceCache.Producer<Mesh>() {
            @Override
            public Mesh create() {
                TeaVMTextureResource theResource = (TeaVMTextureResource) aTexture;
                return Mesh.createMesh(theResource.getTexture());
            }
        }, aZIndex);

        Float32Array theVertices = Float32Array.create(8);
        theVertices.set(0, aX0);
        theVertices.set(1, aY0);
        theVertices.set(2, aX1);
        theVertices.set(3, aY1);
        theVertices.set(4, aX2);
        theVertices.set(5, aY2);
        theVertices.set(6, aX3);
        theVertices.set(7, aY3);
        theMesh.setVertices(theVertices);
    }

    @Override
    public void drawScaled(String aObjectID, final GameResource aResource, int aX, int aY, int aWidth, int aHeight, int aZIndex) {

        Sprite theCurrentObject = instanceCache.getOrCreate(aObjectID, new InstanceCache.Producer<Sprite>() {
            @Override
            public Sprite create() {
                TeaVMTextureResource theTexture = (TeaVMTextureResource) aResource;
                Sprite theSprite =  Sprite.createSprite(theTexture.getTexture());
                theSprite.getScale().set(1, 1);
                return theSprite;
            }
        }, aZIndex);

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aX, aY);
        theCurrentObject.setWidth(aWidth);
        theCurrentObject.setHeight(aHeight);
    }
}