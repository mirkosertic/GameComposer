package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.teavm.pixi.Graphics;
import de.mirkosertic.gameengine.teavm.pixi.Sprite;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

public class TeaVMEffectCanvas implements EffectCanvas {

    private final InstanceCache instanceCache;

    public TeaVMEffectCanvas(InstanceCache aInstanceCacne) {
        instanceCache = aInstanceCacne;
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
    public void fillRect(String aObjectID, int aX, int aY, int aWidth, int aHeight, Color aColor, int aZIndex) {
        /*context.fillRect(aX, aY, aWidth, aHeight);*/
    }

    @Override
    public void fillTriangle(String aObjectID, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, Color aColor, int aZIndex) {
        /*context.beginPath();
        context.moveTo(aX0, aY0);
        context.lineTo(aX1, aY1);
        context.lineTo(aX2, aY2);
        context.closePath();
        context.fill();
        context.stroke();*/
    }

    @Override
    public void fillTriangle(String aObjectID, final GameResource aTexture, final int aX0, final int aY0, final int aX1, final int aY1, final int aX2,
            final int aY2, int aU0, int aV0, int aU1, int aV1, int aU2, int aV2, int aZIndex) {

        /*final Mesh theFilledTriangle = instanceCache.getOrCreate(aObjectID, new InstanceCache.Producer<Mesh>() {
            @Override
            public Mesh create() {
                TeaVMTextureResource theTexture = (TeaVMTextureResource) aTexture;
                float[] thePoints = new float[6];
                thePoints[0] = aX0;
                thePoints[1] = aY0;
                thePoints[2] = aX1;
                thePoints[3] = aY1;
                thePoints[4] = aX2;
                thePoints[5] = aY2;
                Mesh theMesh = Mesh.createMesh(theTexture.getTexture(), thePoints);
                return theMesh;
            }
        });*/

        // Affine Texture Mapping
        /*context.save();

        context.beginPath();
        context.moveTo(aX0, aY0);
        context.lineTo(aX1, aY1);
        context.lineTo(aX2, aY2);
        context.closePath();
        context.clip();

        aX1 -= aX0; aY1 -= aY0; aX2 -= aX0; aY2 -= aY0;
        aU1 -= aU0; aV1 -= aV0; aU2 -= aU0; aV2 -= aV0;

        double id = 1.0 / (aU1*aV2 - aU2*aV1);
        double a = id * (aV2*aX1 - aV1*aX2);
        double b = id * (aV2*aY1 - aV1*aY2);
        double c = id * (aU1*aX2 - aU2*aX1);
        double d = id * (aU1*aY2 - aU2*aY1);
        double e = aX0 - a*aU0 - c*aV0;
        double f = aY0 - b*aU0 - d*aV0;

        context.transform( a, b, c, d, e, f );
        TeaVMGameResource theBitmap = (TeaVMGameResource) aTexture;
        context.drawImage(theBitmap.getElement(), 0, 0);

        context.restore();*/

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