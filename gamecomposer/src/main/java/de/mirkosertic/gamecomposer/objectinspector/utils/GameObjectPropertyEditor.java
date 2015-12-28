package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class GameObjectPropertyEditor implements PropertyEditor<GameObject> {

    private final PropertyEditorItem<GameObject> item;
    private final ComboBox<GameObject> items;

    public GameObjectPropertyEditor(PropertySheet.Item aItem) {
        item = (PropertyEditorItem<GameObject>) aItem;
        items = new ComboBox<>();
        items.setConverter(new StringConverter<GameObject>() {
            @Override
            public String toString(GameObject object) {
                return object.nameProperty().get();
            }

            @Override
            public GameObject fromString(String string) {
                return null;
            }
        });
        items.setOnAction(event -> item.setValue(getValue()));

        GameScene theScene = (GameScene) item.getOwner();
        for (GameObject theObject : theScene.getObjects()) {
            items.getItems().add(theObject);
        }
    }

    @Override
    public Node getEditor() {
        return items;
    }

    @Override
    public GameObject getValue() {
        return items.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setValue(GameObject aValue) {
        items.getSelectionModel().select(aValue);
    }
}