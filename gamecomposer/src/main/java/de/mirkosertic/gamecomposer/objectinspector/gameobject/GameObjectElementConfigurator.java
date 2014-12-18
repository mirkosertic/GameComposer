package de.mirkosertic.gamecomposer.objectinspector.gameobject;

import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.SizePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplate;
import de.mirkosertic.gameengine.physic.PlatformBehaviorTemplate;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplate;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplate;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.text.TextBehaviorTemplate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = GameObject.class)
public class GameObjectElementConfigurator implements ObjectInspectorElementConfigurator<GameObject> {

    private static final String CATEGORY_NAME = "03 GameObject";

    @Inject
    MessageBox messageBox;

    @Override
    public List<PropertySheet.Item> getItemsFor(final GameObject aObject) {
        ArrayList<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the object", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.sizeProperty(), "Size", "The size of the object", Optional.of(SizePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.visibleProperty(), "Visible", "The visibility of the object"));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete object...", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete object", "Do you really want to delete this item?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                    aObject.getGameScene().removeGameObject(aObject);
                }
            }
        }));
        theResult.add(theActions);

        List<ActionPropertyEditorItem.Action> theAddBehaviorActions = new ArrayList<>();
        if (aObject.getBehaviorTemplate(PhysicsBehaviorTemplate.class) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add physics behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new PhysicsBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(PlatformBehaviorTemplate.class) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add platform behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new PlatformBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(PlayerScoreBehaviorTemplate.class) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add player score behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new PlayerScoreBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(SpriteBehaviorTemplate.class) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add sprite behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new SpriteBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(StaticBehaviorTemplate.class) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add static behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new StaticBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(TextBehaviorTemplate.class) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add text behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new TextBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(ConstantMovementBehaviorTemplate.class) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add constant movement behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new ConstantMovementBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }

        if (!theAddBehaviorActions.isEmpty()) {
            ActionPropertyEditorItem theAddActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
            for (ActionPropertyEditorItem.Action theSingleAction : theAddBehaviorActions) {
                theAddActions.addAction(theSingleAction);
            }
            theResult.add(theAddActions);
        }

        return theResult;
    }
}