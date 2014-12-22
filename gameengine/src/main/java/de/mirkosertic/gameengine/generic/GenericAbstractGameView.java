package de.mirkosertic.gameengine.generic;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.*;

import java.io.IOException;

public abstract class GenericAbstractGameView<S extends GameResource> implements GameView {

    private GameRuntime gameRuntime;
    private CameraBehavior cameraBehavior;
    private GestureDetector gestureDetector;
    private Size currentScreenSize;

    public GenericAbstractGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        gameRuntime = aGameRuntime;
        cameraBehavior = aCameraBehavior;
        gestureDetector = aGestureDetector;
    }

    public void setCurrentScreenSize(Size aCurrentScreenSize) {
        this.currentScreenSize = aCurrentScreenSize;
    }

    public Size getCurrentScreenSize() {
        return currentScreenSize;
    }

    public void prepareNewScene(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        cameraBehavior = aCameraBehavior;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;
    }

    protected abstract boolean beginFrame(GameScene aScene);

    protected abstract void beforeInstance(GameObjectInstance aInstance, float aOffsetX, float aOffsetY, Angle aRotation);

    protected abstract void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, S aResource, float aPositionX, float aPositionY);

    protected abstract void drawText(GameObjectInstance aInstance, Position aPosition, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText, Size aSize);

    protected abstract void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Color aColor, float aX, float aY, float aWidth, float aHeight);

    protected abstract void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen);

    protected void framefinished() {
    }

    protected abstract void logError(String aMessage);

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene, RuntimeStatistics aStatistics) {

        if (!beginFrame(aScene)) {
            return;
        }

        for (GameObjectInstance theInstance : cameraBehavior.getObjectsToDrawInRightOrder(aScene)) {

            Position thePosition = cameraBehavior.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            Angle theAngle = theInstance.rotationAngleProperty().get();

            beforeInstance(theInstance, thePosition.x + theHalfWidth, thePosition.y + theHalfHeight, theAngle);

            Sprite theSpriteBehavior = theInstance.getBehavior(SpriteBehavior.class);
            if (theSpriteBehavior != null && !theSpriteBehavior.resourceNameProperty().isNull()) {

                ResourceName theSpriteResource = theSpriteBehavior.resourceNameProperty().get();

                try {
                    S theGameResource = gameRuntime.getResourceCache()
                            .getResourceFor(theSpriteResource);

                    drawImage(theInstance, thePosition, theGameResource, -theHalfWidth, -theHalfHeight);

                } catch (IOException e) {
                    logError("Error while rendering sprite " + theSpriteResource.name);
                }
            } else {
                Text theTextBehavior = theInstance.getBehavior(TextBehavior.class);
                if (theTextBehavior != null) {
                    ExpressionParser theExpressionParser = aScene.get(theTextBehavior.textExpressionProperty().get());

                    drawText(theInstance, thePosition, theTextBehavior.fontProperty().get(),
                            theTextBehavior.colorProperty().get(), theExpressionParser.evaluateToString(), theSize);
                } else {
                    drawRect(theInstance, thePosition, Color.WHITE, -theHalfWidth, -theHalfHeight, theSize.width, theSize.height);
                }
            }

            afterInstance(theInstance, thePosition);
        }

        framefinished();
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public CameraBehavior getCameraBehavior() {
        return cameraBehavior;
    }
}