package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscene;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.RunSceneAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import javax.inject.Inject;

public class RunSceneEditorController implements ActionController {

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
        if (!aAction.gameSceneProperty().isNull()) {
            scenes.getSelectionModel().select(aAction.gameSceneProperty().get());
        }
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