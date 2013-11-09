package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import de.mirkosertic.gameengine.types.Color;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

import java.io.IOException;

public class GWTGameView implements GameView {

    private GameRuntime gameRuntime;
    private Canvas canvas;
    private CameraComponent cameraComponent;
    private Size currentSize;
    private int counter;

    public GWTGameView(GameRuntime aGameRuntime, Canvas aCanvas, CameraComponent aCameraComponent) {
        gameRuntime = aGameRuntime;
        canvas = aCanvas;
        cameraComponent = aCameraComponent;
    }

    public void setSize(Size aSize) {
        currentSize = aSize;
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {
        Context2d theContext = canvas.getContext2d();

        Color theBGColor = aScene.getBackgroundColor();

        CssColor theCssBackground = CssColor.make(theBGColor.getR(), theBGColor.getG(), theBGColor.getB());
        theContext.setFillStyle(theCssBackground);
        theContext.setStrokeStyle(theCssBackground);
        theContext.fillRect(0, 0, currentSize.width, currentSize.height);

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(aScene)) {

            Position thePosition = cameraComponent.transformToScreenPosition(theInstance.positionProperty().get());
            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            theContext.save();

            theContext.translate(thePosition.x + theHalfWidth, thePosition.y + theHalfHeight);
            theContext.rotate(theInstance.rotationAngleProperty().get().toRadians());

            SpriteComponentTemplate theTemplateComponent = theInstance.getOwnerGameObject().getComponentTemplate(SpriteComponentTemplate.class);
            if (theTemplateComponent != null) {
                try {
                    GWTBitmapResource theBitmap = gameRuntime.getResourceCache().getResourceFor(theTemplateComponent.getResourceName());
                    drawGameObjectInstance(theContext, theInstance, new Position(-theHalfWidth, -theHalfHeight), theSize, theBitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                theContext.setFillStyle(CssColor.make(255, 255, 255));
                theContext.setStrokeStyle(CssColor.make(255, 255, 255));
                theContext.setLineWidth(1);
                theContext.strokeRect(-theHalfWidth, -theHalfHeight, theSize.width, theSize.height);
            }

            theContext.restore();
        }
        counter++;
        theContext.setFillStyle(CssColor.make(255, 255, 255));
        theContext.setStrokeStyle(CssColor.make(255, 255, 255));
        theContext.fillText("Frame " + counter, 50, 50);
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
