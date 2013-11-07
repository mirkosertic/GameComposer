package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.NewGameSceneEvent;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.core.Game;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameEditorController implements ChildController {

    @FXML
    TextField nameTextField;

    @FXML
    ComboBox defaultSceneComboBox;

    @Inject
    Event<ObjectUpdatedEvent> objectUpdatedEvent;

    @Inject
    Event<NewGameSceneEvent> newGameSceneEvent;

    private Parent view;
    private Game game;

    public GameEditorController initialize(Parent aView, Game aObject) {
        view = aView;
        game = aObject;

        nameTextField.setText(game.getName());

        defaultSceneComboBox.getItems().clear();
        defaultSceneComboBox.getItems().addAll(aObject.getScenes());
        defaultSceneComboBox.setValue(aObject.getDefaultScene());

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                game.setName(aNewValue);
                objectUpdatedEvent.fire(new ObjectUpdatedEvent(game));
            }
        });
        defaultSceneComboBox.itemsProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object aOldValue, Object aNewValue) {
                game.setDefaultScene((String) aNewValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    @FXML
    public void onNewScene() {
        newGameSceneEvent.fire(new NewGameSceneEvent());
    }
}