package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.GameSceneDeletedEvent;
import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.*;
import de.mirkosertic.gamecomposer.objectinspector.utils.ColorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.GameObjectPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.starfield.StarfieldGameSceneEffect;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.*;

@ObjectInspectorElementConfiguratorType(clazz = GameScene.class)
public class GameSceneElementConfigurator implements ObjectInspectorElementConfigurator<GameScene> {

    private static final String CATEGORY_NAME = "02 Scene";

    interface EffectDescription {

        String description();

        GameSceneEffect create();
    }

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

        Map<Class<? extends GameSceneEffect>, EffectDescription> theAvailableEffects = new HashMap<>();
        theAvailableEffects.put(StarfieldGameSceneEffect.class, new EffectDescription() {
            @Override
            public String description() {
                return "Starfield";
            }

            @Override
            public GameSceneEffect create() {
                return new StarfieldGameSceneEffect(aObject, aObject.getRuntime().getEventManager());
            }
        });

        for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
            theAvailableEffects.remove(theEffect.getClass());
        }

        for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
            theAvailableEffects.remove(theEffect.getClass());
        }

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
        for (Map.Entry<Class<? extends GameSceneEffect>, EffectDescription> theEntry : theAvailableEffects.entrySet()) {
            theActions.addAction(new ActionPropertyEditorItem.Action("Add " + theEntry.getValue().description()+" effect", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent aEvent) {
                    aObject.addEffect(theEntry.getValue().create());
                }
            }));
        }
        theResult.add(theActions);

        //TODO: Implement Layout bound editing here
        return theResult;
    }
}