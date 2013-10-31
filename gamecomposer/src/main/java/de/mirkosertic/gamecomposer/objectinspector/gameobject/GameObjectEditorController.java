package de.mirkosertic.gamecomposer.objectinspector.gameobject;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.core.GameObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class GameObjectEditorController implements ChildController {

    @FXML
    TextField uuidTextField;

    @FXML
    TextField nameTextField;

    private Parent view;
    private GameObject gameObject;

    public GameObjectEditorController initialize(Parent aView, GameObject aObject) {
        view = aView;
        gameObject = aObject;

        uuidTextField.setText(gameObject.getUuid());
        nameTextField.setText(gameObject.getName());

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                gameObject.setName(aNewValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}