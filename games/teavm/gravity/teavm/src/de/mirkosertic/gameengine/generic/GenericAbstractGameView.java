package de.mirkosertic.gameengine.generic;

import de.mirkosertic.gameengine.Version;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.*;

import java.io.IOException;
import java.util.List;

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

    protected abstract void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Angle aRotation);

    protected abstract void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, S aResource);

    protected abstract void drawText(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText, Size aSize);

    protected abstract void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor, Size aSize);

    protected abstract void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen);

    protected void framefinished() {
    }

    protected abstract void logError(String aMessage);

    protected abstract EffectCanvas createEffectCanvas();

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene, RuntimeStatistics aStatistics) {

        if (!beginFrame(aScene)) {
            return;
        }

        List<GameObjectInstance> theVisibleInstances = cameraBehavior.getObjectsToDrawInRightOrder();

        EffectCanvas theEffectCanvas = createEffectCanvas();

        // Run the preprocessors
        for (GameSceneEffect theEffect : aScene.getPreprocessorEffects()) {
            theEffect.render(theEffectCanvas, theVisibleInstances, cameraBehavior);
        }

        for (GameObjectInstance theInstance : theVisibleInstances) {

            Position thePositionOnScreen = cameraBehavior.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            Position theCenterOffset = new Position(theHalfWidth, theHalfHeight);

            Angle theAngle = theInstance.rotationAngleProperty().get();

            beforeInstance(theInstance, thePositionOnScreen, theCenterOffset, theAngle);

            boolean theSomethingRendered = false;

            SpriteBehavior theSpriteBehavior = theInstance.getBehavior(SpriteBehavior.class);
            if (theSpriteBehavior != null) {

                ResourceName theSpriteResource = theSpriteBehavior.computeCurrentFrame(aGameTime);
                if (theSpriteResource != null) {
                    try {
                        S theGameResource = gameRuntime.getResourceCache()
                                .getResourceFor(theSpriteResource);

                        drawImage(theInstance, thePositionOnScreen, theCenterOffset, theGameResource);

                        theSomethingRendered = true;

                    } catch (IOException e) {
                        logError("Error while rendering sprite " + theSpriteResource.name);
                    }
                }
            }
            TextBehavior theTextBehavior = theInstance.getBehavior(TextBehavior.class);
            if (theTextBehavior != null) {
                TextExpression theExpression = theTextBehavior.textExpressionProperty().get();
                String theTextToDraw;
                if (theTextBehavior.isScriptProperty().get()) {
                    // Scripting is enabled, so we have to evaluate the expression
                    try {
                        LUAScriptEngine theEngine = gameRuntime.getScriptEngineFactory()
                                .createNewEngine(theExpression);

                        theTextToDraw = theEngine.evaluateSimpleExpressionFor(theInstance);
                    } catch (Exception e) {
                        // Failed to process the script
                        // can be IOException
                        // or more likely compile errors
                        theTextToDraw = "Processing error : " + e.getMessage();
                    }
                } else {
                    theTextToDraw = theExpression.expression;
                }

                drawText(theInstance, thePositionOnScreen, theCenterOffset, theTextBehavior.fontProperty().get(),
                        theTextBehavior.colorProperty().get(), theTextToDraw, theSize);


                theSomethingRendered = true;
            }

            if (!theSomethingRendered) {
                // Nothing was rendered.
                drawRect(theInstance, thePositionOnScreen, theCenterOffset, Color.WHITE, theSize);
            }

            afterInstance(theInstance, thePositionOnScreen);
        }

        // Run the postprocessors
        for (GameSceneEffect theEffect : aScene.getPostprocessorEffects()) {
            theEffect.render(theEffectCanvas, theVisibleInstances, cameraBehavior);
        }

        // Shall we print Debug Information to the Screen?
        if (aScene.getGame().enableDebugProperty().get()) {
            Color theDebugColor = new Color(255,0,0);
            Size theTextSize = new Size(200,10);
            Position theCenterOffset = new Position(100, 5);
            Font theFont = new Font(Font.FontName.ARIAL, 10);
            // Draw version information
            AbsolutePositionAnchor theAnchor = AbsolutePositionAnchor.BOTTOM_LEFT;
            drawTextAt(theAnchor, new Position(0, 30), theCenterOffset, theTextSize, theFont, theDebugColor, Version.VERSION);
            drawTextAt(theAnchor, new Position(0, 20), theCenterOffset, theTextSize, theFont, theDebugColor,
                    "Time for every frame : " + aStatistics.getAverageTimePerLoopCycle() + " ms");
            drawTextAt(theAnchor, new Position(0, 10), theCenterOffset, theTextSize, theFont, theDebugColor,
                    "Number of visible instances : " + theVisibleInstances.size());
        }

        framefinished();
    }

    private void drawTextAt(AbsolutePositionAnchor aAnchor, Position aPosition, Position aCenterOffset, Size aSize, Font aFont, Color aColor, String aText) {

        Position thePositionOnScreen = aAnchor.compute(aPosition, getCurrentScreenSize());

        beforeInstance(null, thePositionOnScreen, aCenterOffset, new Angle(0));
        drawText(null, thePositionOnScreen, aCenterOffset, aFont, aColor, aText, aSize);
        afterInstance(null, thePositionOnScreen);
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public CameraBehavior getCameraBehavior() {
        return cameraBehavior;
    }
}