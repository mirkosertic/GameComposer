package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gamecomposer.objectinspector.PersistentPropertyEditorItem;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class ScenePropertyEditor implements PropertyEditor<String> {

    private final PersistentPropertyEditorItem<String> item;
    private final ComboBox<GameScene> scenes;

    public ScenePropertyEditor(PropertySheet.Item aItem) {
        item = (PersistentPropertyEditorItem<String>) aItem;
        scenes = new ComboBox<>();
        scenes.setConverter(new StringConverter<GameScene>() {
            @Override
            public String toString(GameScene object) {
                return object.nameProperty().get();
            }

            @Override
            public GameScene fromString(String string) {
                return null;
            }
        });
        for (String theSceneID : item.getPersistenceManager().getScenes()) {
            GameScene theScene = item.getPersistenceManager().getScene(theSceneID);
            scenes.getItems().add(theScene);
        }
    }

    @Override
    public Node getEditor() {
        return scenes;
    }

    @Override
    public String getValue() {
        GameScene theScene = scenes.getSelectionModel().getSelectedItem();
        if (theScene != null) {
            return theScene.nameProperty().get();
        }
        return null;
    }

    @Override
    public void setValue(String aValue) {
        if (aValue == null) {
            scenes.getSelectionModel().clearSelection();
        } else {
            scenes.getSelectionModel().select(item.getPersistenceManager().getScene(aValue));
        }
    }
}