package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.core.Game;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class GameEditorController implements ChildController {

    @FXML
    TextField nameTextField;

    private Parent view;
    private Game game;

    public GameEditorController initialize(Parent aView, Game aObject) {
        view = aView;
        game= aObject;

        nameTextField.setText(game.getName());

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                game.setName(aNewValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}