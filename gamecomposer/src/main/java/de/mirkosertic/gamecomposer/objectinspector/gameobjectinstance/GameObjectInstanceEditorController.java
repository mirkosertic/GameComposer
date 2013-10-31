package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class GameObjectInstanceEditorController implements ChildController {

    @FXML
    TextField nameTextField;

    private Parent view;
    private GameObjectInstance object;

    public GameObjectInstanceEditorController initialize(Parent aView, GameObjectInstance aObject) {
        view = aView;
        object = aObject;

        nameTextField.setText(object.getName());

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                object.setName(aNewValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}