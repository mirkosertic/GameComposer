package de.mirkosertic.gamecomposer.contentarea.gamescene;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.physic.GamePhysicsManager;
import de.mirkosertic.gameengine.physic.PhysicsDebugCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Rectangle;
import de.mirkosertic.gameengine.type.Size;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EditorJXGameView extends JavaFXGameView {

    private Object selectedObject;
    private Position currentMousePosition;
    private final GamePhysicsManager physicsManager;

    private final BooleanProperty snapToGrid;
    private final BooleanProperty renderPhysicsDebug;
    private final IntegerProperty gridsizeWidth;
    private final IntegerProperty gridsizeHeight;

    public EditorJXGameView(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GamePhysicsManager aPhysicsManager) {
        super(aGameRuntime, aCameraComponent);

        snapToGrid = new SimpleBooleanProperty(true);
        renderPhysicsDebug = new SimpleBooleanProperty(false);
        gridsizeWidth = new SimpleIntegerProperty(64);
        gridsizeHeight = new SimpleIntegerProperty(64);

        physicsManager = aPhysicsManager;

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseEntered(aEvent);
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseExited(aEvent);
            }
        });
        setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseMoved(aEvent);
            }
        });
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
    protected void afterRendering(final GraphicsContext aContext) {
        super.afterRendering(aContext);

        if (snapToGridProperty().get()) {

            CameraBehavior theCameraComponent = getCameraComponent();
            int theGridsizeWidth = gridsizeWidthProperty().get();
            int theGridsizeHeight = gridsizeHeightProperty().get();

            Position theCameraPosition = theCameraComponent.getObjectInstance().positionProperty().get();
            Size theScreenSize = theCameraComponent.getScreenSize();

            float theStartX = theCameraPosition.x - (theCameraPosition.x % theGridsizeWidth);
            float theStartY = theCameraPosition.y - (theCameraPosition.y % theGridsizeHeight);
            int theCols = theScreenSize.width / theGridsizeWidth + 1;
            int theRows = theScreenSize.height / theGridsizeHeight + 1;

            for (int theRow = 0; theRow < theRows; theRow++) {
                for (int theCol = 0; theCol < theCols; theCol++) {
                    float theX = theStartX + theCol * theGridsizeWidth;
                    float theY = theStartY + theRow * theGridsizeHeight;
                    Position theScreenPosition = theCameraComponent.transformToScreenPosition(new Position(theX, theY));

                    aContext.setFill(Color.SILVER);
                    aContext.setStroke(Color.SILVER);
                    aContext.setLineWidth(1);

                    if (theCol == 0) {
                        aContext.strokeLine(0, theScreenPosition.y, theScreenSize.width, theScreenPosition.y);
                    }

                    aContext.strokeLine(theScreenPosition.x, 0, theScreenPosition.x, theScreenSize.height);
                }
            }
        }

        if (renderPhysicsDebug.get()) {
            physicsManager.drawDebug(new PhysicsDebugCanvas() {
                @Override
                public void drawLine(Position p1, Position p2, boolean awake) {

                    CameraBehavior theCameraComponent = getCameraComponent();

                    Position theScreenP1 = theCameraComponent.transformToScreenPosition(p1);
                    Position theScreenP2 = theCameraComponent.transformToScreenPosition(p2);

                    if (awake) {
                        aContext.setFill(Color.RED);
                        aContext.setStroke(Color.RED);
                        aContext.setLineWidth(3);
                    } else {
                        aContext.setFill(Color.BLUE);
                        aContext.setStroke(Color.BLUE);
                        aContext.setLineWidth(2);
                    }
                    aContext.strokeLine(theScreenP1.x, theScreenP1.y, theScreenP2.x, theScreenP2.y);
                }

                @Override
                public void drawPosition(Position aPosition) {

                    CameraBehavior theCameraComponent = getCameraComponent();

                    Position theScreenP1 = theCameraComponent.transformToScreenPosition(aPosition);

                    aContext.setFill(Color.BLUE);
                    aContext.setStroke(Color.BLUE);
                    aContext.setLineWidth(3);
                    aContext.fillOval(theScreenP1.x, theScreenP1.y, 5, 5);
                }
            });
        }

        GameScene theGameScene = getGameScene();
        Rectangle theLayoutBounds = theGameScene.layoutBoundsProperty().get();

        CameraBehavior theCameraComponent = getCameraComponent();
        GameObjectInstance theCamera = theCameraComponent.getObjectInstance();
        Position theCurrentCameraPosition = theCamera.positionProperty().get();

        // Draw layout bounds
        double theBoundsX = theLayoutBounds.position.x - theCurrentCameraPosition.x;
        double theBoundsY = theLayoutBounds.position.y - theCurrentCameraPosition.y;

        aContext.setStroke(Color.YELLOW);
        aContext.setLineWidth(3);

        aContext.strokeRect(theBoundsX, theBoundsY, theLayoutBounds.size.width, theLayoutBounds.size.height);
    }

    @Override
    protected void drawText(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText, Size aSize) {
        super.drawText(aContext, aInstance, aPosition, aFont, aColor, aText, aSize);

        javafx.scene.text.Font theFont = toFont(aFont);
        FontMetrics theMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(theFont);
        int theWidth = (int) theMetrics.computeStringWidth(aText);
        int theHeight = (int) theMetrics.getLineHeight();

        drawSelectionAndHighlighting(aContext, aInstance, aPosition, new Size(theWidth, theHeight));
    }

    @Override
    protected void drawGameObjectInstance(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, Size aSize, JavaFXBitmapResource aBitmapResource) {
        super.drawGameObjectInstance(aContext, aInstance, aPosition, aSize, aBitmapResource);

        drawSelectionAndHighlighting(aContext, aInstance, aPosition, aSize);
    }

    void drawSelectionAndHighlighting(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, Size aSize) {

        SavedState theSavedState = saveState(aContext);

        if (aInstance == selectedObject || aInstance.getOwnerGameObject() == selectedObject) {
            aContext.setFill(Color.WHITE);
            aContext.setStroke(Color.WHITE);
            aContext.setLineWidth(1);
            aContext.strokeRect(aPosition.x, aPosition.y, aSize.width, aSize.height);
        }
        if (currentMousePosition != null) {
            boolean theHighlighted = false;
            if (aInstance.absolutePositionProperty().get()) {
                if (aInstance.contains(currentMousePosition)) {
                    theHighlighted = true;
                }
            } else {
                if (aInstance.contains(getCameraComponent().transformFromScreen(currentMousePosition))) {
                    theHighlighted = true;
                }
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

                aContext.setFill(theColor);
                aContext.setStroke(theColor);
                aContext.setLineWidth(3);

                int theOffset = 5;
                Position theTopLeft = new Position(aPosition.x - theOffset, aPosition.y - theOffset);
                Position theTopRight = new Position(aPosition.x + aSize.width + theOffset, aPosition.y - theOffset);
                Position theBottomLeft = new Position(aPosition.x - theOffset, aPosition.y + aSize.height + theOffset);
                Position theBottomRight = new Position(aPosition.x + aSize.width + theOffset, aPosition.y + aSize.height + theOffset);

                // Top left
                aContext.strokeLine(theTopLeft.x, theTopLeft.y, theTopLeft.x + theOffset, theTopLeft.y);
                aContext.strokeLine(theTopLeft.x, theTopLeft.y, theTopLeft.x, theTopLeft.y + theOffset);

                // Top right
                aContext.strokeLine(theTopRight.x, theTopLeft.y, theTopRight.x - theOffset, theTopRight.y);
                aContext.strokeLine(theTopRight.x, theTopRight.y, theTopRight.x, theTopRight.y + theOffset);

                // Bottom left
                aContext.strokeLine(theBottomLeft.x, theBottomLeft.y, theBottomLeft.x + theOffset, theBottomRight.y);
                aContext.strokeLine(theBottomLeft.x, theBottomLeft.y, theBottomLeft.x, theBottomLeft.y - theOffset);

                // Bottom right
                aContext.strokeLine(theBottomRight.x, theBottomRight.y, theBottomRight.x - theOffset, theBottomRight.y);
                aContext.strokeLine(theBottomRight.x, theBottomRight.y, theBottomRight.x, theBottomRight.y - theOffset);

            }
        }

        restoreState(aContext, theSavedState);
    }
}