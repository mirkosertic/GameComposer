package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscene;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.GameAssetSelector;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.action.PlaySoundAction;
import de.mirkosertic.gameengine.action.RunSceneAction;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.types.ResourceName;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import javax.inject.Inject;

public class RunSceneEditorController implements Controller {

    @FXML
    ComboBox scenes;

    @Inject
    PersistenceManager persistenceManager;

    private Node view;
    private RunSceneAction action;

    RunSceneEditorController initialize(Node aView, RunSceneAction aAction) {
        view = aView;
        action = aAction;

        scenes.getItems().clear();
        scenes.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String aSceneId) {
                GameScene theScene = persistenceManager.getScene(aSceneId);
                return theScene.nameProperty().get();
            }

            @Override
            public String fromString(String aValue) {
                // Not required here
                return null;
            }
        });
        scenes.getItems().addAll(persistenceManager.getScenes());
        scenes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                action.gameSceneProperty().set((String) scenes.getSelectionModel().getSelectedItem());
            }
        });
        return this;
    }

    public Node getView() {
        return view;
    }
}