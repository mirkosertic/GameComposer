package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gamecomposer.NewGameSceneEvent;
import de.mirkosertic.gameengine.core.Game;

import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameEditorController implements ObjectInspectorElementController {

    @FXML
    TextField nameTextField;

    @FXML
    ComboBox defaultSceneComboBox;

    @Inject
    Event<Object> eventGateway;

    @Inject
    PersistenceManager persistenceManager;

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
        defaultSceneComboBox.getItems().addAll(persistenceManager.getScenes());
        defaultSceneComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String aSceneID) {
                GameScene theScene = persistenceManager.getScene(aSceneID);
                return theScene.nameProperty().get();
            }

            @Override
            public String fromString(String s) {
                return null;
            }
        });

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