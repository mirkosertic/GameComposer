package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.types.Angle;
import de.mirkosertic.gameengine.types.Position;

public class GameObjectInstanceEditorController implements ChildController {

    @FXML
    TextField nameTextField;

    @FXML
    TextField xTextField;

    @FXML
    TextField yTextField;

    @FXML
    TextField rotationTextField;

    @FXML
    Hyperlink jumpToObject;

    @Inject
    Event<Object> eventGateway;

    private Parent view;
    private GameObjectInstance object;

    public GameObjectInstanceEditorController initialize(Parent aView, GameObjectInstance aObject) {
        view = aView;
        object = aObject;

        nameTextField.setText(object.nameProperty().get());
        xTextField.setText(Integer.toString((int) aObject.positionProperty().get().x));
        yTextField.setText(Integer.toString((int) aObject.positionProperty().get().y));
        rotationTextField.setText(Integer.toString(aObject.rotationAngleProperty().get().angleInDegrees));

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                object.nameProperty().set(aNewValue);
                eventGateway.fire(new ObjectUpdatedEvent(object));
            }
        });
        xTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Position thePosition = object.positionProperty().get();
                    object.positionProperty().set(new Position(Integer.valueOf(aNewValue), thePosition.y));
                    eventGateway.fire(new ObjectUpdatedEvent(object));
                }
            }
        });
        yTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Position thePosition = object.positionProperty().get();
                    object.positionProperty().set(new Position(thePosition.x, Integer.valueOf(aNewValue)));
                    eventGateway.fire(new ObjectUpdatedEvent(object));
                }
            }
        });
        rotationTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    object.rotationAngleProperty().set(new Angle(Integer.valueOf(aNewValue)));
                    eventGateway.fire(new ObjectUpdatedEvent(object));
                }
            }
        });
        jumpToObject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                eventGateway.fire(new ObjectSelectedEvent(object.getOwnerGameObject()));
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}
