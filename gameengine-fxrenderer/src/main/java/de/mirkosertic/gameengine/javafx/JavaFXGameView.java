package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.sprites.Sprite;
import de.mirkosertic.gameengine.sprites.SpriteComponent;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextComponent;
import de.mirkosertic.gameengine.types.Angle;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;
import de.mirkosertic.gameengine.types.TextExpression;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

import java.io.IOException;

public class JavaFXGameView extends Canvas implements GameView {

    private AnimationTimer animationTimer;
    private GameScene gameScene;
    private final GameRuntime gameRuntime;
    private final CameraComponent cameraComponent;

    protected JavaFXGameView(GameRuntime aRuntime, CameraComponent aCameraComponent) {
        cameraComponent = aCameraComponent;
        gameRuntime = aRuntime;
    }

    private void renderScene() {
        GraphicsContext theContext = getGraphicsContext2D();

        de.mirkosertic.gameengine.types.Color theBGColor = gameScene.backgroundColorProperty().get();

        Color theFXColor = Color.rgb(theBGColor.r, theBGColor.g, theBGColor.b);
        theContext.setFill(theFXColor);
        theContext.setStroke(theFXColor);
        theContext.fillRect(0, 0, getWidth(), getHeight());

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(gameScene)) {

            Position thePosition = cameraComponent.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            theContext.save();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            Angle theAngle = theInstance.rotationAngleProperty().get();
            int theAngleInDegrees = theAngle.angleInDegrees;
            if (theAngleInDegrees % 360 != 0) {
                Rotate r = new Rotate(theAngleInDegrees, thePosition.x + theHalfWidth, thePosition.y + theHalfHeight);
                theContext.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            }

            boolean theSomethingRendered = false;

            Sprite theTemplateComponent = theInstance.getComponent(SpriteComponent.class);
            if (theTemplateComponent != null && !theTemplateComponent.resourceNameProperty().isNull()) {
                try {
                    JavaFXBitmapResource theBitmap = gameRuntime.getResourceCache().getResourceFor(
                            theTemplateComponent.resourceNameProperty().get());
                    drawGameObjectInstance(theContext, theInstance, thePosition, theSize, theBitmap);

                    theSomethingRendered = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Text theTextComponent = theInstance.getComponent(TextComponent.class);
            if (theTextComponent != null) {
                drawText(theContext, theInstance, thePosition, theTextComponent.fontProperty().get(), theTextComponent.colorProperty().get(), theTextComponent.textExpressionProperty().get(), theSize);
                theSomethingRendered = true;
            }

            if (!theSomethingRendered) {
                theContext.setFill(Color.WHITE);
                theContext.setStroke(Color.WHITE);
                theContext.setLineWidth(1);
                theContext.strokeRect(thePosition.x, thePosition.y, theSize.width, theSize.height);
            }

            theContext.restore();
        }
        afterRendering(theContext);
    }

    protected Font toFont(de.mirkosertic.gameengine.types.Font aFont) {
        switch (aFont.name) {
            case ARIAL:
                return new Font("Arial", aFont.size);
            case VERDANA:
                return new Font("Verdana", aFont.size);
        }
        throw new IllegalArgumentException("Wrong font name : "+aFont.name);
    }

    protected void afterRendering(GraphicsContext aContext) {
    }

    protected void drawText(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, de.mirkosertic.gameengine.types.Font aFont, de.mirkosertic.gameengine.types.Color aColor, TextExpression aExpression, Size aSize) {
        aContext.save();
        Color theTextColor = Color.rgb(aColor.r, aColor.g, aColor.b);
        aContext.setFill(theTextColor);
        aContext.setStroke(theTextColor);
        aContext.setFont(toFont(aFont));
        aContext.fillText(aExpression.expression, aPosition.x, aPosition.y + aFont.size);
        aContext.restore();
    }

    protected void drawGameObjectInstance(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition,
            Size aSize, JavaFXBitmapResource aBitmapResource) {
        aContext.drawImage(aBitmapResource, aPosition.x, aPosition.y);
    }

    public void startTimer(final GameLoop aGameLoop) {
        if (animationTimer == null) {
            gameScene = aGameLoop.getScene();
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    aGameLoop.singleRun();
                    renderScene();
                }
            };
            animationTimer.start();
        }
    }

    public void stopTimer() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {
        // Do just nothing here, as the real rendering is done by the animation timer
    }

    protected GameScene getGameScene() {
        return gameScene;
    }

    protected CameraComponent getCameraComponent() {
        return cameraComponent;
    }
}