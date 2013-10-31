package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class GameSceneEditorController implements ChildController {

    @FXML
    TextField nameTextField;

    private Parent view;
    private GameScene gameScene;

    public GameSceneEditorController initialize(Parent aView, GameScene aObject) {
        view = aView;
        gameScene = aObject;

        nameTextField.setText(gameScene.getName());

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                gameScene.setName(aNewValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}