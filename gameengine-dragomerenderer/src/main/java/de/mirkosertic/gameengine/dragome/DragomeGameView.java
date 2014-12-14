package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.ExpressionParser;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RuntimeStatistics;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.Size;

import java.io.IOException;

class DragomeGameView implements GameView {

    private Size currentScreenSize;

    private CameraBehavior cameraComponent;
    private GestureDetector gestureDetector;
    private GameRuntime gameRuntime;

    private final DragomeCanvas canvas;

    public DragomeGameView(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GestureDetector aGestureDetector, DragomeCanvas aCanvas) {
        cameraComponent = aCameraComponent;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;
        canvas = aCanvas;
    }

    public void prepareNewScene(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GestureDetector aGestureDetector) {
        cameraComponent = aCameraComponent;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene, RuntimeStatistics aStatistics) {

        DragomeCanvasRenderingContext2D theContext = canvas.getContext("2d");

        Color theBGColor = aScene.backgroundColorProperty().get();

        theContext.setFillStyle("rgb(" + theBGColor.r + "," + theBGColor.g + "," + theBGColor.b + ")");
        theContext.clearRect(0, 0, currentScreenSize.width, currentScreenSize.height);

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(aScene)) {

            theContext.save();

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

            Sprite theSpriteComponent = theInstance.getBehavior(SpriteBehavior.class);
            if (theSpriteComponent != null) {

                ResourceName theSpriteResource = theSpriteComponent.resourceNameProperty().get();

                try {
                    DragomeGameResource theGameResource = gameRuntime.getResourceCache()
                            .getResourceFor(theSpriteResource);

                    theContext.drawImage(theGameResource.getElement(), -theHalfWidth, -theHalfHeight);

                } catch (IOException e) {
                    DragomeLogger.error("Error while loading game resource "+theSpriteResource.name+" : " + e);
                }
            } else {
                Text theTextComponent = theInstance.getBehavior(TextBehavior.class);
                if (theTextComponent != null) {
                    ExpressionParser theExpressionParser = aScene.get(theTextComponent.textExpressionProperty().get());

                    drawText(theContext, thePosition, theTextComponent.fontProperty().get(),
                            theTextComponent.colorProperty().get(), theExpressionParser.evaluateToString(), theSize);

                } else {
                    String theWhite = "rgb(255,255,255)";
                    theContext.setFillStyle(theWhite);
                    theContext.setStrokeStyle(theWhite);
                    theContext.setLineWidth(1);
                    theContext.strokeRect(-theHalfWidth, -theHalfHeight, theSize.width, theSize.height);
                }
            }

            theContext.restore();
        }
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setSize(Size aSize) {
        currentScreenSize = aSize;
        canvas.setWidth(aSize.width);
        canvas.setHeight(aSize.height);
    }

    String toFont(de.mirkosertic.gameengine.type.Font aFont) {
        switch (aFont.name) {
            case ARIAL:
                return Integer.toString(aFont.size)+"px arial";
            case VERDANA:
                return Integer.toString(aFont.size)+"px verdana";
        }
        throw new IllegalArgumentException("Wrong font name : "+aFont.name);
    }

    void drawText(DragomeCanvasRenderingContext2D aContext, Position aPosition, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText, Size aSize) {
        String theTextColor = "rgb(" + aColor.r + "," + aColor.g + "," + aColor.b + ")";
        aContext.setFillStyle(theTextColor);
        aContext.setStrokeStyle(theTextColor);
        aContext.setFont(toFont(aFont));
        aContext.fillText(aText, aPosition.x, aPosition.y + aFont.size);
    }
}