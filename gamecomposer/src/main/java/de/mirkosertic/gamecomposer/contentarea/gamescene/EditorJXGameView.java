package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.Position;
import de.mirkosertic.gameengine.core.Size;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EditorJXGameView extends JavaFXGameView {

    private Object selectedObject;
    private Position currentMousePosition;

    public EditorJXGameView(GameResourceCache aResourceCache, CameraComponent aCameraComponent) {
        super(aResourceCache, aCameraComponent);

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

    @Override
    protected void drawGameObjectInstance(GraphicsContext aContext, GameObjectInstance aInstance, Position aPosition, Size aSize, JavaFXBitmapResource aBitmapResource) {
        super.drawGameObjectInstance(aContext, aInstance, aPosition, aSize, aBitmapResource);

        aContext.save();
        if (aInstance == selectedObject || aInstance.getOwnerGameObject() == selectedObject) {
            aContext.setFill(Color.WHITE);
            aContext.setStroke(Color.WHITE);
            aContext.setLineWidth(1);
            aContext.strokeRect(aPosition.getX(), aPosition.getY(), aSize.getWidth(), aSize.getHeight());
        }
        if (currentMousePosition != null) {
            if (aInstance.contains(currentMousePosition)) {
                aContext.setFill(Color.GREEN);
                aContext.setStroke(Color.GREEN);
                aContext.setLineWidth(3);
                aContext.strokeRect(aPosition.getX(), aPosition.getY(), aSize.getWidth(), aSize.getHeight());
            }
        }
        aContext.restore();
    }
}
