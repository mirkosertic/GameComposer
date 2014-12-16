package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.AbsolutePositionAnchorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.AnglePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.PositionPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = GameObjectInstance.class)
public class GameObjectInstanceElementConfigurator implements ObjectInspectorElementConfigurator<GameObjectInstance> {

    private static final String CATEGORY_NAME = "05 Instance";

    @Inject
    MessageBox messageBox;

    @Override
    public List<PropertySheet.Item> getItemsFor(final GameObjectInstance aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the scene", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.positionProperty(), "Position", "The position of the instannce", Optional.of(PositionPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.visibleProperty(), "Visible", "Is the instance visible or not", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.rotationAngleProperty(), "Rotation angle", "The rotation angle of the instance", Optional.of(AnglePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.absolutePositionProperty(), "Absolute position", "Is the instances position absolute?", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.absolutePositionAnchorProperty(), "Position anchor", "The position anchor of the instance", Optional.of(AbsolutePositionAnchorPropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete object...", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete object", "Do you really want to delete this item?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                    aObject.getOwnerGameObject().getGameScene().removeGameObjectInstance(aObject);
                }
            }
        }));
        theResult.add(theActions);

        return theResult;
    }
}
