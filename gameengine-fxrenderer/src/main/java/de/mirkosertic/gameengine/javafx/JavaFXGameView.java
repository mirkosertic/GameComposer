/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.EffectCanvas;
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

public class JavaFXGameView extends GenericAbstractGameView<JavaFXBitmapResource> {

    protected static class SavedState {
        public Paint fill;
        public Paint stroke;
        public Affine transform;
        public Font font;
        public double lineWidth;
    }

    private AnimationTimer animationTimer;
    private GameScene gameScene;
    private final Canvas canvasNode;

    public JavaFXGameView(GameRuntime aRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        super(aRuntime, aCameraBehavior, aGestureDetector);
        canvasNode = new Canvas();
    }

    private GraphicsContext context;
    private SavedState savedState;

    protected GraphicsContext getContext() {
        return context;
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        context = canvasNode.getGraphicsContext2D();

        savedState = saveState(context);

        de.mirkosertic.gameengine.type.Color theBGColor = gameScene.backgroundColorProperty().get();

        Color theFXColor = Color.rgb(theBGColor.r, theBGColor.g, theBGColor.b);
        context.setFill(theFXColor);
        context.setStroke(theFXColor);
        context.fillRect(0, 0, canvasNode.getWidth(), canvasNode.getHeight());

        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new JavaFXEffectCanvas(context);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Angle aRotation) {
        int theAngleInDegrees = aRotation.angleInDegrees;
        if (theAngleInDegrees % 360 != 0) {
            Rotate r = new Rotate(theAngleInDegrees, aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
            context.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        }
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, JavaFXBitmapResource aResource) {
        context.drawImage(aResource, aPositionOnScreen.x, aPositionOnScreen.y);
    }

    @Override
    protected void drawText(String aInstanceID, Position aPosition, Angle aAngle, Position aCenterOffset, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText,
            Size aSize, boolean aVisible) {
        Color theTextColor = Color.rgb(aColor.r, aColor.g, aColor.b);
        context.setFill(theTextColor);
        context.setStroke(theTextColor);
        context.setFont(toFont(aFont));
        context.fillText(aText, aPosition.x, aPosition.y + aFont.size);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, de.mirkosertic.gameengine.type.Color aColor, Size aSize) {
        context.setFill(Color.WHITE);
        context.setStroke(Color.WHITE);
        context.setLineWidth(1);
        context.strokeRect(aPositionOnScreen.x, aPositionOnScreen.y, aSize.width, aSize.height);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        restoreState(savedState);
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

    protected void restoreState(SavedState aState) {
        context.setFill(aState.fill);
        context.setStroke(aState.stroke);
        context.setTransform(aState.transform);
        context.setFont(aState.font);
        context.setLineWidth(aState.lineWidth);
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

    public void startTimer(final GameLoop aGameLoop, final boolean aEditingMode) {
        if (animationTimer == null) {
            gameScene = aGameLoop.getScene();
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    if (aEditingMode) {
                        aGameLoop.singleRunOnlyUpdateGameView();
                    } else {
                        aGameLoop.singleRun();
                    }
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

    protected GameScene getGameScene() {
        return gameScene;
    }

    public Canvas getCanvasNode() {
        return canvasNode;
    }
}