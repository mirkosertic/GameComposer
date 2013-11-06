package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.Position;
import de.mirkosertic.gameengine.core.Size;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameObjectInstanceEditorController implements ChildController {

    @FXML
    TextField nameTextField;

    @FXML
    TextField xTextField;

    @FXML
    TextField yTextField;

    @FXML
    Hyperlink jumpToObject;

    @Inject
    Event<ObjectUpdatedEvent> objectUpdatedEvent;

    @Inject
    Event<ObjectSelectedEvent> objectSelectedEvent;

    private Parent view;
    private GameObjectInstance object;

    public GameObjectInstanceEditorController initialize(Parent aView, GameObjectInstance aObject) {
        view = aView;
        object = aObject;

        nameTextField.setText(object.getName());
        xTextField.setText(Integer.toString((int) aObject.getPosition().x));
        yTextField.setText(Integer.toString((int) aObject.getPosition().y));

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                object.setName(aNewValue);
                objectUpdatedEvent.fire(new ObjectUpdatedEvent(object));
            }
        });
        xTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Position thePosition = object.getPosition();
                    object.getOwnerGameObject().getGameScene().updateObjectInstancePosition(object, new Position(Integer.valueOf(aNewValue), thePosition.y));
                    objectUpdatedEvent.fire(new ObjectUpdatedEvent(object));
                }
            }
        });
        yTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Position thePosition = object.getPosition();
                    object.getOwnerGameObject().getGameScene().updateObjectInstancePosition(object, new Position(thePosition.x, Integer.valueOf(aNewValue)));
                    objectUpdatedEvent.fire(new ObjectUpdatedEvent(object));
                }
            }
        });
        jumpToObject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                objectSelectedEvent.fire(new ObjectSelectedEvent(object.getOwnerGameObject()));
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}