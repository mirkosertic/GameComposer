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
package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.physic.GamePhysicsManager;
import de.mirkosertic.gameengine.physic.PhysicsDebugCanvas;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Rectangle;
import de.mirkosertic.gameengine.type.Size;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EditorFXGameView extends JavaFXGameView {

    private Object selectedObject;
    private Position currentMousePosition;
    private final GamePhysicsManager physicsManager;

    private final BooleanProperty snapToGrid;
    private final BooleanProperty renderPhysicsDebug;
    private final IntegerProperty gridsizeWidth;
    private final IntegerProperty gridsizeHeight;

    public EditorFXGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector, GamePhysicsManager aPhysicsManager) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);

        snapToGrid = new SimpleBooleanProperty(true);
        renderPhysicsDebug = new SimpleBooleanProperty(false);
        gridsizeWidth = new SimpleIntegerProperty(64);
        gridsizeHeight = new SimpleIntegerProperty(64);

        physicsManager = aPhysicsManager;

        getCanvasNode().setOnMouseEntered(aEvent -> onMouseEntered(aEvent));
        getCanvasNode().setOnMouseExited(aEvent -> onMouseExited(aEvent));
        getCanvasNode().setOnMouseMoved(aEvent -> onMouseMoved(aEvent));
    }

    public BooleanProperty snapToGridProperty() {
        return snapToGrid;
    }

    public BooleanProperty renderPhysicsDebugProperty() {
        return renderPhysicsDebug;
    }

    public IntegerProperty gridsizeWidthProperty() {
        return gridsizeWidth;
    }

    public IntegerProperty gridsizeHeightProperty() {
        return gridsizeHeight;
    }

    private void onMouseEntered(MouseEvent aEvent) {
    }

    private void onMouseExited(MouseEvent aEvent) {
        currentMousePosition = null;
    }

    private void onMouseMoved(MouseEvent aEvent) {
        currentMousePosition = new Position((int) aEvent.getX(), (int) aEvent.getY());
    }

    public void onObjectSelected(ObjectSelectedEvent aEvent) {
        selectedObject = aEvent.getSelectedObject();
    }

    Position snapToGrid(Position aPosition) {
        int theGridsizeWidth = gridsizeWidthProperty().get();
        int theGridsizeHeight = gridsizeHeightProperty().get();

        return aPosition.snapToGrid(theGridsizeWidth, theGridsizeHeight);
    }

    @Override
    protected void framefinished() {
        super.framefinished();

        GraphicsContext theContext = getContext();

        CameraBehavior theCameraBehavior = getCameraBehavior();

        if (snapToGridProperty().get()) {

            int theGridsizeWidth = gridsizeWidthProperty().get();
            int theGridsizeHeight = gridsizeHeightProperty().get();

            Position theCameraPosition = theCameraBehavior.getInstance().positionProperty().get();
            Size theScreenSize = theCameraBehavior.getScreenSize();

            float theStartX = theCameraPosition.x - (theCameraPosition.x % theGridsizeWidth);
            float theStartY = theCameraPosition.y - (theCameraPosition.y % theGridsizeHeight);
            int theCols = theScreenSize.width / theGridsizeWidth + 1;
            int theRows = theScreenSize.height / theGridsizeHeight + 1;

            for (int theRow = 0; theRow < theRows; theRow++) {
                for (int theCol = 0; theCol < theCols; theCol++) {
                    float theX = theStartX + theCol * theGridsizeWidth;
                    float theY = theStartY + theRow * theGridsizeHeight;
                    Position theScreenPosition = theCameraBehavior.transformToScreenPosition(new Position(theX, theY));

                    theContext.setFill(Color.SILVER);
                    theContext.setStroke(Color.SILVER);
                    theContext.setLineWidth(1);

                    if (theCol == 0) {
                        theContext.strokeLine(0, theScreenPosition.y, theScreenSize.width, theScreenPosition.y);
                    }

                    theContext.strokeLine(theScreenPosition.x, 0, theScreenPosition.x, theScreenSize.height);
                }
            }
        }

        if (renderPhysicsDebug.get()) {
            physicsManager.drawDebug(new PhysicsDebugCanvas() {
                @Override
                public void drawLine(Position p1, Position p2, boolean awake) {

                    Position theScreenP1 = theCameraBehavior.transformToScreenPosition(p1);
                    Position theScreenP2 = theCameraBehavior.transformToScreenPosition(p2);

                    if (awake) {
                        theContext.setFill(Color.RED);
                        theContext.setStroke(Color.RED);
                        theContext.setLineWidth(3);
                    } else {
                        theContext.setFill(Color.BLUE);
                        theContext.setStroke(Color.BLUE);
                        theContext.setLineWidth(2);
                    }
                    theContext.strokeLine(theScreenP1.x, theScreenP1.y, theScreenP2.x, theScreenP2.y);
                }

                @Override
                public void drawPosition(Position aPosition) {

                    Position theScreenP1 = theCameraBehavior.transformToScreenPosition(aPosition);

                    theContext.setFill(Color.BLUE);
                    theContext.setStroke(Color.BLUE);
                    theContext.setLineWidth(3);
                    theContext.fillOval(theScreenP1.x, theScreenP1.y, 5, 5);
                }
            });
        }

        GameScene theGameScene = getGameScene();
        Rectangle theLayoutBounds = theGameScene.layoutBoundsProperty().get();

        GameObjectInstance theCamera = theCameraBehavior.getInstance();
        Position theCurrentCameraPosition = theCamera.positionProperty().get();

        // Draw layout bounds
        double theBoundsX = theLayoutBounds.position.x - theCurrentCameraPosition.x;
        double theBoundsY = theLayoutBounds.position.y - theCurrentCameraPosition.y;

        theContext.setStroke(Color.YELLOW);
        theContext.setLineWidth(3);

        theContext.strokeRect(theBoundsX, theBoundsY, theLayoutBounds.size.width, theLayoutBounds.size.height);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {

        super.afterInstance(aInstance, aPositionOnScreen);

        Size theSize = aInstance.getOwnerGameObject().sizeProperty().get();
        GraphicsContext theContext = getContext();

        SavedState theSavedState = saveState(theContext);

        if (aInstance == selectedObject || aInstance.getOwnerGameObject() == selectedObject) {
            theContext.setFill(Color.WHITE);
            theContext.setStroke(Color.WHITE);
            theContext.setLineWidth(1);
            theContext.strokeRect(aPositionOnScreen.x, aPositionOnScreen.y, theSize.width, theSize.height);
        }
        if (currentMousePosition != null) {
            boolean theHighlighted = false;

            if (aPositionOnScreen.x <= currentMousePosition.x &&
                    aPositionOnScreen.y <= currentMousePosition.y &&
                    aPositionOnScreen.x + theSize.width >= currentMousePosition.x &&
                    aPositionOnScreen.y + theSize.height >= currentMousePosition.y) {
                theHighlighted = true;
            }

            if (theHighlighted) {
                int theColorOffset = (int) ((System.currentTimeMillis() / 2) % 400);

                int theRed = 0;
                int theGreen;
                if (theColorOffset > 200) {
                    theGreen = 55 + 400 - theColorOffset;
                } else {
                    theGreen = 55 + theColorOffset;
                }
                int theBlue = 0;

                Color theColor = Color.rgb(theRed, theGreen, theBlue);

                theContext.setFill(theColor);
                theContext.setStroke(theColor);
                theContext.setLineWidth(3);

                int theOffset = 5;
                Position theTopLeft = new Position(aPositionOnScreen.x - theOffset, aPositionOnScreen.y - theOffset);
                Position theTopRight = new Position(aPositionOnScreen.x + theSize.width + theOffset, aPositionOnScreen.y - theOffset);
                Position theBottomLeft = new Position(aPositionOnScreen.x - theOffset, aPositionOnScreen.y + theSize.height + theOffset);
                Position theBottomRight = new Position(aPositionOnScreen.x + theSize.width + theOffset, aPositionOnScreen.y + theSize.height + theOffset);

                // Top left
                theContext.strokeLine(theTopLeft.x, theTopLeft.y, theTopLeft.x + theOffset, theTopLeft.y);
                theContext.strokeLine(theTopLeft.x, theTopLeft.y, theTopLeft.x, theTopLeft.y + theOffset);

                // Top right
                theContext.strokeLine(theTopRight.x, theTopLeft.y, theTopRight.x - theOffset, theTopRight.y);
                theContext.strokeLine(theTopRight.x, theTopRight.y, theTopRight.x, theTopRight.y + theOffset);

                // Bottom left
                theContext.strokeLine(theBottomLeft.x, theBottomLeft.y, theBottomLeft.x + theOffset, theBottomRight.y);
                theContext.strokeLine(theBottomLeft.x, theBottomLeft.y, theBottomLeft.x, theBottomLeft.y - theOffset);

                // Bottom right
                theContext.strokeLine(theBottomRight.x, theBottomRight.y, theBottomRight.x - theOffset, theBottomRight.y);
                theContext.strokeLine(theBottomRight.x, theBottomRight.y, theBottomRight.x, theBottomRight.y - theOffset);

            }
        }

        restoreState(theSavedState);
    }
}