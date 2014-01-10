package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.*;

import java.io.IOException;

public class GWTCanvasGameView extends AbstractWebGameView {

    private final GameRuntime gameRuntime;
    private final Canvas canvas;
    private final CameraBehavior cameraComponent;
    private final GestureDetector gestureDetector;
    private int counter;

    public GWTCanvasGameView(GameRuntime aGameRuntime, Canvas aCanvas, CameraBehavior aCameraComponent) {
        gameRuntime = aGameRuntime;
        canvas = aCanvas;
        cameraComponent = aCameraComponent;
        gestureDetector = new DefaultGestureDetector(aGameRuntime.getEventManager());
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {
        Size theCurrentSize = getSize();

        Context2d theContext = canvas.getContext2d();

        SavedState theSavedState = saveState(theContext);

        Color theBGColor = aScene.backgroundColorProperty().get();

        CssColor theCssBackground = CssColor.make(theBGColor.r, theBGColor.g, theBGColor.b);
        theContext.setFillStyle(theCssBackground);
        theContext.setStrokeStyle(theCssBackground);
        theContext.fillRect(0, 0, theCurrentSize.width, theCurrentSize.height);

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(aScene)) {

            Position thePosition = cameraComponent.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            Angle theAngle = theInstance.rotationAngleProperty().get();
            int theAngleInDegrees = theAngle.angleInDegrees;
            theContext.translate(thePosition.x + theHalfWidth, thePosition.y + theHalfHeight);
            if (theAngleInDegrees % 360 != 0) {
                theContext.rotate(theAngle.toRadians());
            }

            boolean theSomethingRendered = false;

            Sprite theSpriteComponent = theInstance.getComponent(SpriteBehavior.class);
            if (theSpriteComponent != null) {
                try {
                    GWTBitmapResource theBitmap = gameRuntime.getResourceCache().getResourceFor(theSpriteComponent.resourceNameProperty().get());
                    drawGameObjectInstance(theContext, theInstance, new Position(-theHalfWidth, -theHalfHeight), theSize, theBitmap);

                    theSomethingRendered = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Text theTextComponent = theInstance.getComponent(TextBehavior.class);
            if (theTextComponent != null) {
                ExpressionParser theExpressionParser = aScene.get(theTextComponent.textExpressionProperty().get());
                drawText(theContext, thePosition, theTextComponent.fontProperty().get(), theTextComponent.colorProperty().get(), theExpressionParser.evaluateToString(), theSize);
                theSomethingRendered = true;
            }

            if (!theSomethingRendered) {
                theContext.setFillStyle(CssColor.make(255, 255, 255));
                theContext.setStrokeStyle(CssColor.make(255, 255, 255));
                theContext.setLineWidth(1);
                theContext.strokeRect(-theHalfWidth, -theHalfHeight, theSize.width, theSize.height);
            }

            restoreState(theContext, theSavedState);
        }
        counter++;
        theContext.setFillStyle(CssColor.make(255, 255, 255));
        theContext.setStrokeStyle(CssColor.make(255, 255, 255));
        theContext.fillText("Frame " + counter, 50, 50);
    }

    void drawGameObjectInstance(Context2d aContext, GameObjectInstance aInstance, Position aPosition, Size aSize, GWTBitmapResource aBitmapResource) {
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