package de.mirkosertic.gamecomposer.objectinspector.gameobject;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.Size;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameObjectEditorController implements ChildController {

    @FXML
    TextField uuidTextField;

    @FXML
    TextField nameTextField;

    @FXML
    TextField widthTextField;

    @FXML
    TextField heightTextField;

    @Inject
    Event<ObjectUpdatedEvent> objectUpdatedEvent;

    private Parent view;
    private GameObject gameObject;

    public GameObjectEditorController initialize(Parent aView, GameObject aObject) {
        view = aView;
        gameObject = aObject;

        uuidTextField.setText(gameObject.getUuid());
        nameTextField.setText(gameObject.getName());
        widthTextField.setText(Integer.toString(aObject.getSize().width));
        heightTextField.setText(Integer.toString(aObject.getSize().height));

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                gameObject.setName(aNewValue);
            }
        });
        widthTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Size theSize = gameObject.getSize();
                    gameObject.getGameScene().updateObjectSize(gameObject, new Size(Integer.valueOf(aNewValue), theSize.height));
                    objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameObject));
                }
            }
        });
        heightTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Size theSize = gameObject.getSize();
                    gameObject.getGameScene().updateObjectSize(gameObject, new Size(theSize.width, Integer.valueOf(aNewValue)));
                    objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameObject));
                }
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}