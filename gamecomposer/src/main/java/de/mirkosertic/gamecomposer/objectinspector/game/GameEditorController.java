package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.PropertyBinder;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorChildController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import de.mirkosertic.gamecomposer.NewGameSceneEvent;
import de.mirkosertic.gameengine.core.Game;

public class GameEditorController implements ObjectInspectorChildController {

    @FXML
    TextField nameTextField;

    @FXML
    ComboBox defaultSceneComboBox;

    @Inject
    Event<Object> eventGateway;

    private Parent view;
    private Game game;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(game.nameProperty());
        PropertyBinder.unbind(game.defaultSceneProperty());
    }

    public GameEditorController initialize(Parent aView, Game aObject) {
        view = aView;
        game = aObject;

        defaultSceneComboBox.getItems().clear();
        defaultSceneComboBox.getItems().addAll(aObject.getScenes());

        PropertyBinder.bind(aObject.nameProperty(), nameTextField.textProperty());
        PropertyBinder.bind(aObject.defaultSceneProperty(), defaultSceneComboBox.valueProperty());

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    @FXML
    public void onNewScene() {
        eventGateway.fire(new NewGameSceneEvent());
    }
}
