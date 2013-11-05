package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.Position;
import de.mirkosertic.gameengine.core.Size;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.physics.GamePhysicsManager;
import de.mirkosertic.gameengine.physics.PhysicsDebugCanvas;
import de.mirkosertic.gameengine.resource.GameResourceCache;
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
    private GamePhysicsManager physicsManager;

    private BooleanProperty snapToGrid;
    private BooleanProperty renderPhysicsDebug;
    private IntegerProperty gridsizeWidth;
    private IntegerProperty gridsizeHeight;

    public EditorJXGameView(GameResourceCache aResourceCache, CameraComponent aCameraComponent, GamePhysicsManager aPhysicsManager) {
        super(aResourceCache, aCameraComponent);

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

        float theSnapX = aPosition.x - aPosition.x % theGridsizeWidth;
        float theSnapY = aPosition.y - aPosition.y % theGridsizeHeight;
        return new Position(theSnapX, theSnapY);
    }

    @Override
    protected void afterRendering(final GraphicsContext aContext) {
        super.afterRendering(aContext);

        if (snapToGridProperty().get()) {

            CameraComponent theCameraComponent = getCameraComponent();
            int theGridsizeWidth = gridsizeWidthProperty().get();
            int theGridsizeHeight = gridsizeHeightProperty().get();

            Position theCameraPosition = theCameraComponent.getObjectInstance().getPosition();
            Size theScreenSize = theCameraComponent.getScreenSize();

            float theStartX = theCameraPosition.x - theCameraPosition.x % theGridsizeWidth;
            float theStartY = theCameraPosition.y - theCameraPosition.y % theGridsizeHeight;
            int theCols = (int) (theScreenSize.width / theGridsizeWidth) + 1;
            int theRows = (int) (theScreenSize.height / theGridsizeHeight) + 1;

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

                    aContext.strokeLine(theX, 0, theX, theScreenSize.height);
                }
            }
        }

        if (renderPhysicsDebug.get()) {
            physicsManager.drawDebug(new PhysicsDebugCanvas() {
                @Override
                public void drawLine(Position p1, Position p2) {

                    aContext.setFill(Color.RED);
                    aContext.setStroke(Color.RED);
                    aContext.setLineWidth(3);
                    aContext.strokeLine(p1.x, p1.y, p2.x, p2.y);
                }

                @Override
                public void drawPosition(Position aPosition) {
                    aContext.setFill(Color.BLUE);
                    aContext.setStroke(Color.BLUE);
                    aContext.setLineWidth(3);
                    aContext.fillOval(aPosition.x, aPosition.y, 5, 5);
                }
            });
        }
    }

    @Override
    protected void drawGameObjectInstance(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, Size aSize, JavaFXBitmapResource aBitmapResource) {
        super.drawGameObjectInstance(aContext, aInstance, aPosition, aSize, aBitmapResource);

        aContext.save();
        if (aInstance == selectedObject || aInstance.getOwnerGameObject() == selectedObject) {
            aContext.setFill(Color.WHITE);
            aContext.setStroke(Color.WHITE);
            aContext.setLineWidth(1);
            aContext.strokeRect(aPosition.x, aPosition.y, aSize.width, aSize.height);
        }
        if (currentMousePosition != null) {
            if (aInstance.contains(currentMousePosition)) {
                aContext.setFill(Color.GREEN);
                aContext.setStroke(Color.GREEN);
                aContext.setLineWidth(3);
                aContext.strokeRect(aPosition.x, aPosition.y, aSize.width, aSize.height);
            }
        }

        aContext.restore();
    }
}
