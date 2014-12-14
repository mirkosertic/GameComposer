package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class ScenePropertyEditor implements PropertyEditor<String> {

    private final PropertyEditorItem<String> item;
    private final ComboBox<GameScene> scenes;

    public ScenePropertyEditor(PropertySheet.Item aItem) {
        item = (PropertyEditorItem<String>) aItem;
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
        for (String theSceneID : item.getEditorContext().getPersistenceManager().getScenes()) {
            GameScene theScene = item.getEditorContext().getPersistenceManager().getScene(theSceneID);
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
            scenes.getSelectionModel().select(item.getEditorContext().getPersistenceManager().getScene(aValue));
        }
    }
}