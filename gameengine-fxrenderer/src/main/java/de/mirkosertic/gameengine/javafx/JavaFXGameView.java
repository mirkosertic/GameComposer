package de.mirkosertic.gameengine.javafx;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import de.mirkosertic.gameengine.types.Angle;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

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

            SpriteComponentTemplate theTemplateComponent = theInstance.getOwnerGameObject().getComponentTemplate(
                    SpriteComponentTemplate.class);
            if (theTemplateComponent != null && !theTemplateComponent.resourceNameProperty().isNull()) {
                try {
                    JavaFXBitmapResource theBitmap = gameRuntime.getResourceCache().getResourceFor(
                            theTemplateComponent.resourceNameProperty().get());
                    drawGameObjectInstance(theContext, theInstance, thePosition, theSize, theBitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                theContext.setFill(Color.WHITE);
                theContext.setStroke(Color.WHITE);
                theContext.setLineWidth(1);
                theContext.strokeRect(thePosition.x, thePosition.y, theSize.width, theSize.height);
            }

            theContext.restore();
        }
        afterRendering(theContext);
    }

    protected void afterRendering(GraphicsContext aContext) {
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
