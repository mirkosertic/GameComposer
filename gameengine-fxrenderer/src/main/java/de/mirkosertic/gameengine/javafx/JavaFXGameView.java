package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import de.mirkosertic.gameengine.sprites.SpriteComponent;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;

public class JavaFXGameView extends Canvas implements GameView {

    private AnimationTimer animationTimer;
    private GameScene gameScene;
    private GameResourceCache resourceCache;
    private CameraComponent cameraComponent;

    public JavaFXGameView(GameResourceCache aResourceCache, CameraComponent aCameraComponent) {
        cameraComponent = aCameraComponent;
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (gameScene != null) {
                    renderScene();
                }
            }
        };
        resourceCache = aResourceCache;
    }

    private void renderScene() {
        GraphicsContext theContext = getGraphicsContext2D();

        theContext.setFill(Color.BLACK);
        theContext.setStroke(Color.BLACK);
        theContext.fillRect(0,0, getWidth(), getHeight());

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(gameScene)) {
            Position thePosition = cameraComponent.transformToScreenPosition(theInstance.getPosition());
            Size theSize = theInstance.getSize();
            SpriteComponentTemplate theTemplateComponent = theInstance.getOwnerGameObject().getComponentTemplate(SpriteComponentTemplate.class);
            if (theTemplateComponent != null) {
                try {
                    JavaFXBitmapResource theBitmap = resourceCache.getResourceFor(theTemplateComponent.getResourceName());
                    drawGameObjectInstance(theContext, theInstance, thePosition, theSize, theBitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                theContext.setFill(Color.WHITE);
                theContext.setStroke(Color.WHITE);
                theContext.setLineWidth(1);
                theContext.strokeRect(thePosition.getX(), thePosition.getY(), theSize.getWidth(), theSize.getHeight());
            }
        }
    }

    protected void drawGameObjectInstance(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, Size aSize, JavaFXBitmapResource aBitmapResource) {
        aContext.drawImage(aBitmapResource, aPosition.getX(), aPosition.getY());
    }

    public void startTimer() {
        animationTimer.start();
    }

    public void stopTimer() {
        animationTimer.stop();
    }

    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {
        gameScene = aScene;
    }

    protected GameScene getGameScene() {
        return gameScene;
    }

    protected CameraComponent getCameraComponent() {
        return cameraComponent;
    }
}
