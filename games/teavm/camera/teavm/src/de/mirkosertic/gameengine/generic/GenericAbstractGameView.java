package de.mirkosertic.gameengine.generic;

import de.mirkosertic.gameengine.Callback;
import de.mirkosertic.gameengine.Version;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RuntimeStatistics;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.AbsolutePositionAnchor;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;

public abstract class GenericAbstractGameView<S extends GameResource> implements GameView {

    private static final Position THE_DEBUG_CENTER = new Position(100, 5);
    private static final Size THE_DEBUG_TEXT_SIZE = new Size(300, 10);
    private static final Color THE_DEBUG_TEXT_COLOR = new Color(255, 0, 0);
    private static final Font THE_DEBUG_FONT = new Font(Font.FontName.ARIAL, 10);
    private static final Position THE_DEBUG_POSITION_VERSION = new Position(0, 30);
    private static final Position THE_DEBUG_FRAME_RATE = new Position(0, 20);
    private static final Position THE_DEBUG_VIVISBLE_INSTANCES = new Position(0, 10);

    private GameRuntime gameRuntime;
    private CameraBehavior cameraBehavior;
    private GestureDetector gestureDetector;
    private Size currentScreenSize;

    private static class CachedLUAText {

        private long gameTime;
        private String value;

        public CachedLUAText(long aGameTime, String aValue) {
            gameTime = aGameTime;
            value = aValue;
        }

        public boolean needsUpdate(long aCurrentGameTime) {
            return gameTime  + 60 <= aCurrentGameTime;
        }

        public void update(long aCurrentGameTime, String aNewValue) {
            gameTime = aCurrentGameTime;
            value = aNewValue;
        }
    }

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

    protected abstract void drawText(String aObjectUUID, Position aPositionOnScreen, Angle aAngle, Position aCenterOffset, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText, Size aSize);

    protected abstract void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor, Size aSize);

    protected abstract void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen);

    protected void framefinished() {
    }

    protected abstract void logError(String aMessage);

    protected abstract EffectCanvas createEffectCanvas();

    @Override
    public void renderGame(final long aGameTime, final long aElapsedTimeSinceLastLoop, final GameScene aScene, RuntimeStatistics aStatistics) {

        if (!beginFrame(aScene)) {
            return;
        }

        final EffectCanvas theEffectCanvas = createEffectCanvas();

        // Run the preprocessors
        for (GameSceneEffect theEffect : aScene.getPreprocessorEffects()) {
            theEffect.render(theEffectCanvas, cameraBehavior);
        }

        int theNumberOfInstances = cameraBehavior.processVisibleInstances(new Callback<GameObjectInstance>() {
            @Override
            public void process(GameObjectInstance aValue) {
                Position thePositionOnScreen = cameraBehavior.transformToScreenPosition(aValue);

                Size theSize = aValue.getOwnerGameObject().sizeProperty().get();

                float theHalfWidth = theSize.width / 2;
                float theHalfHeight = theSize.height / 2;

                Position theCenterOffset = new Position(theHalfWidth, theHalfHeight);

                Angle theAngle = aValue.rotationAngleProperty().get();

                beforeInstance(aValue, thePositionOnScreen, theCenterOffset, theAngle);

                boolean theSomethingRendered = false;

                SpriteBehavior theSpriteBehavior = aValue.getBehavior(SpriteBehavior.class);
                if (theSpriteBehavior != null) {

                    ResourceName theSpriteResource = theSpriteBehavior.computeCurrentFrame(aGameTime);
                    if (theSpriteResource != null) {
                        try {
                            S theGameResource = gameRuntime.getResourceCache()
                                    .getResourceFor(theSpriteResource);

                            drawImage(aValue, thePositionOnScreen, theCenterOffset, theGameResource);

                            theSomethingRendered = true;

                        } catch (IOException e) {
                            logError("Error while rendering sprite " + theSpriteResource.name);
                        }
                    }
                }
                TextBehavior theTextBehavior = aValue.getBehavior(TextBehavior.class);
                if (theTextBehavior != null) {
                    TextExpression theExpression = theTextBehavior.textExpressionProperty().get();

                    String theTextToDraw;
                    if (theTextBehavior.isScriptProperty().get()) {

                        String theCacheKey = aValue.uuidProperty().get() + ".luatext";
                        CachedLUAText theCachedValue = aScene.getObjectForKey(theCacheKey);

                        if (theCachedValue == null || theCachedValue.needsUpdate(aGameTime)) {
                            // Scripting is enabled, so we have to evaluate the expression
                            try {
                                LUAScriptEngine theEngine = gameRuntime.getScriptEngineFactory()
                                        .createNewEngine(aScene, theExpression);

                                theTextToDraw = theEngine.evaluateSimpleExpressionFor(aValue);
                            } catch (Exception e) {
                                // Failed to process the script
                                // can be IOException
                                // or more likely compile errors
                                theTextToDraw = "Processing error : " + e.getMessage();
                            }

                            if (theCachedValue == null) {
                                theCachedValue = new CachedLUAText(aGameTime, theTextToDraw);
                                aScene.setObjectForKey(theCacheKey, theCachedValue);
                            } else {
                                theCachedValue.update(aGameTime, theTextToDraw);
                            }
                        } else {
                            theTextToDraw = theCachedValue.value;
                        }
                    } else {
                        theTextToDraw = theExpression.expression;
                    }

                    drawText(aValue.uuidProperty().get(), thePositionOnScreen, aValue.rotationAngleProperty().get(), theCenterOffset, theTextBehavior.fontProperty().get(),
                            theTextBehavior.colorProperty().get(), theTextToDraw, theSize);


                    theSomethingRendered = true;
                }

                if (!theSomethingRendered) {
                    // Nothing was rendered.
                    drawRect(aValue, thePositionOnScreen, theCenterOffset, Color.WHITE, theSize);
                }

                afterInstance(aValue, thePositionOnScreen);
            }
        });

        // Run the postprocessors
        for (GameSceneEffect theEffect : aScene.getPostprocessorEffects()) {
            theEffect.render(theEffectCanvas, cameraBehavior);
        }

        // Shall we print Debug Information to the Screen?
        if (aScene.getGame().enableDebugProperty().get()) {
            // Draw version information
            AbsolutePositionAnchor theAnchor = AbsolutePositionAnchor.BOTTOM_LEFT;
            drawTextAt("debug1", theAnchor, THE_DEBUG_POSITION_VERSION, THE_DEBUG_CENTER, THE_DEBUG_TEXT_SIZE, THE_DEBUG_FONT, THE_DEBUG_TEXT_COLOR, Version.VERSION);
            drawTextAt("debug2", theAnchor, THE_DEBUG_FRAME_RATE, THE_DEBUG_CENTER, THE_DEBUG_TEXT_SIZE, THE_DEBUG_FONT, THE_DEBUG_TEXT_COLOR,
                    "Time for every frame : " + aStatistics.getAverageTimePerLoopCycle() + " ms");
            drawTextAt("debug3", theAnchor, THE_DEBUG_VIVISBLE_INSTANCES, THE_DEBUG_CENTER, THE_DEBUG_TEXT_SIZE, THE_DEBUG_FONT, THE_DEBUG_TEXT_COLOR,
                    "Number of visible instances : " + theNumberOfInstances);
        }

        framefinished();
    }

    private void drawTextAt(String aID, AbsolutePositionAnchor aAnchor, Position aPosition, Position aCenterOffset, Size aSize, Font aFont, Color aColor, String aText) {

        Position thePositionOnScreen = aAnchor.compute(aPosition, getCurrentScreenSize());

        beforeInstance(null, thePositionOnScreen, aCenterOffset, Angle.ZERO);
        drawText(aID, thePositionOnScreen, Angle.ZERO, aCenterOffset, aFont, aColor, aText, aSize);
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