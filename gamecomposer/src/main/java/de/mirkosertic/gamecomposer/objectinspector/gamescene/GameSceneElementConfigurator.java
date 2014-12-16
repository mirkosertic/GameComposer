package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.GameSceneDeletedEvent;
import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.*;
import de.mirkosertic.gamecomposer.objectinspector.utils.ColorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.GameObjectPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.GameObjectConfigurationChanged;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = GameScene.class)
public class GameSceneElementConfigurator implements ObjectInspectorElementConfigurator<GameScene> {

    private static final String CATEGORY_NAME = "02 Scene";

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    MessageBox messageBox;

    @Inject
    Event<GameSceneDeletedEvent> event;

    @Override
    public List<PropertySheet.Item> getItemsFor(final GameScene aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the scene", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.cameraObjectProperty(), "Camera", "The default camera object", Optional.of(GameObjectPropertyEditor.class)));
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.defaultPlayerProperty(), "Default player", "The default player object", Optional.of(GameObjectPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.backgroundColorProperty(), "Background color", "The scene background color", Optional.of(ColorPropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete scene...", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete scene", "Do you really want to delete the scene?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                    persistenceManager.deleteScene(aObject);
                    event.fire(new GameSceneDeletedEvent());
                }
            }
        }));
        theResult.add(theActions);

        //TODO: Implement Layout bound editing here
        return theResult;
    }
}