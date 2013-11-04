package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;

import java.io.IOException;

public class GWTGameView implements GameView {

    private GameResourceCache resourceCache;
    private Canvas canvas;
    private CameraComponent cameraComponent;
    private Size currentSize;
    private int counter;

    public GWTGameView(GameResourceCache aResourceCache, Canvas aCanvas, CameraComponent aCameraComponent) {
        resourceCache = aResourceCache;
        canvas = aCanvas;
        cameraComponent = aCameraComponent;
    }

    public void setSize(Size aSize) {
        currentSize = aSize;
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {
        Context2d theContext = canvas.getContext2d();

        theContext.setFillStyle(CssColor.make(0, 0, 0));
        theContext.setStrokeStyle(CssColor.make(0, 0, 0));
        theContext.fillRect(0, 0, currentSize.width, currentSize.height);

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(aScene)) {
            Position thePosition = cameraComponent.transformToScreenPosition(theInstance.getPosition());
            Size theSize = theInstance.getSize();
            SpriteComponentTemplate theTemplateComponent = theInstance.getOwnerGameObject().getComponentTemplate(SpriteComponentTemplate.class);
            if (theTemplateComponent != null) {
                try {
                    GWTBitmapResource theBitmap = resourceCache.getResourceFor(theTemplateComponent.getResourceName());
                    drawGameObjectInstance(theContext, theInstance, thePosition, theSize, theBitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                theContext.setFillStyle(CssColor.make(255, 255, 255));
                theContext.setStrokeStyle(CssColor.make(255, 255, 255));
                theContext.setLineWidth(1);
                theContext.strokeRect(thePosition.x, thePosition.y, theSize.width, theSize.height);
            }
        }
        counter++;
        theContext.setFillStyle(CssColor.make(255, 255, 255));
        theContext.setStrokeStyle(CssColor.make(255, 255, 255));
        theContext.fillText("Frame "+counter, 50, 50);
    }

    protected void drawGameObjectInstance(Context2d aContext, GameObjectInstance aInstance, Position aPosition, Size aSize, GWTBitmapResource aBitmapResource) {
        if (aBitmapResource.isLoaded()) {
            aContext.drawImage(aBitmapResource.getImage(), aPosition.x, aPosition.y);
        } else {
            aContext.setFillStyle(CssColor.make(255, 255, 255));
            aContext.setStrokeStyle(CssColor.make(255, 255, 255));
            aContext.setLineWidth(1);
            aContext.strokeRect(aPosition.x, aPosition.y, aSize.width, aSize.height);
        }
    }
}
