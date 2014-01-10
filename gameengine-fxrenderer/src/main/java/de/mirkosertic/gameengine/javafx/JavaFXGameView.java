package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.io.IOException;

public class JavaFXGameView extends Canvas implements GameView {

    protected static class SavedState {
        public Paint fill;
        public Paint stroke;
        public Affine transform;
        public Font font;
        public double lineWidth;
    }

    private AnimationTimer animationTimer;
    private GameScene gameScene;
    private final GameRuntime gameRuntime;
    private final CameraBehavior cameraComponent;
    private final GestureDetector gestureDetector;

    protected JavaFXGameView(GameRuntime aRuntime, CameraBehavior aCameraComponent) {
        cameraComponent = aCameraComponent;
        gameRuntime = aRuntime;
        gestureDetector = new DefaultGestureDetector(aRuntime.getEventManager());
    }

    protected SavedState saveState(GraphicsContext aContext) {
        SavedState theResult = new SavedState();
        theResult.fill = aContext.getFill();
        theResult.stroke = aContext.getStroke();
        theResult.transform = aContext.getTransform();
        theResult.font = aContext.getFont();
        theResult.lineWidth = aContext.getLineWidth();
        return theResult;
    }

    protected void restoreState(GraphicsContext aContext, SavedState aState) {
        aContext.setFill(aState.fill);
        aContext.setStroke(aState.stroke);
        aContext.setTransform(aState.transform);
        aContext.setFont(aState.font);
        aContext.setLineWidth(aState.lineWidth);
    }

    private void renderScene() {
        GraphicsContext theContext = getGraphicsContext2D();

        SavedState theSavedState = saveState(theContext);

        de.mirkosertic.gameengine.type.Color theBGColor = gameScene.backgroundColorProperty().get();

        Color theFXColor = Color.rgb(theBGColor.r, theBGColor.g, theBGColor.b);
        theContext.setFill(theFXColor);
        theContext.setStroke(theFXColor);
        theContext.fillRect(0, 0, getWidth(), getHeight());

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(gameScene)) {

            Position thePosition = cameraComponent.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            Angle theAngle = theInstance.rotationAngleProperty().get();
            int theAngleInDegrees = theAngle.angleInDegrees;
            if (theAngleInDegrees % 360 != 0) {
                Rotate r = new Rotate(theAngleInDegrees, thePosition.x + theHalfWidth, thePosition.y + theHalfHeight);
                theContext.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            }

            boolean theSomethingRendered = false;

            Sprite theTemplateComponent = theInstance.getComponent(SpriteBehavior.class);
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
            Text theTextComponent = theInstance.getComponent(TextBehavior.class);
            if (theTextComponent != null) {
                ExpressionParser theExpressionParser = gameScene.get(theTextComponent.textExpressionProperty().get());
                drawText(theContext, theInstance, thePosition, theTextComponent.fontProperty().get(), theTextComponent.colorProperty().get(), theExpressionParser.evaluateToString(), theSize);
                theSomethingRendered = true;
            }

            if (!theSomethingRendered) {
                theContext.setFill(Color.WHITE);
                theContext.setStroke(Color.WHITE);
                theContext.setLineWidth(1);
                theContext.strokeRect(thePosition.x, thePosition.y, theSize.width, theSize.height);
            }

            restoreState(theContext, theSavedState);
        }
        afterRendering(theContext);
    }

    protected Font toFont(de.mirkosertic.gameengine.type.Font aFont) {
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

    protected void drawText(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText, Size aSize) {
        Color theTextColor = Color.rgb(aColor.r, aColor.g, aColor.b);
        aContext.setFill(theTextColor);
        aContext.setStroke(theTextColor);
        aContext.setFont(toFont(aFont));
        aContext.fillText(aText, aPosition.x, aPosition.y + aFont.size);
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

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {
        // Do just nothing here, as the real rendering is done by the animation timer
    }

    protected GameScene getGameScene() {
        return gameScene;
    }

    protected CameraBehavior getCameraComponent() {
        return cameraComponent;
    }
}