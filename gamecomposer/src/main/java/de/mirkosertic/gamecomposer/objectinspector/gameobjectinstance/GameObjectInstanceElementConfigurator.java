package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.AbsolutePositionAnchorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.AnglePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.PositionPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = GameObjectInstance.class)
public class GameObjectInstanceElementConfigurator implements ObjectInspectorElementConfigurator<GameObjectInstance> {

    private static final String CATEGORY_NAME = "Instance";

    @Override
    public List<PropertySheet.Item> getItemsFor(GameObjectInstance aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the scene", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.positionProperty(), "Position", "The position of the instannce", Optional.of(PositionPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.visibleProperty(), "Visible", "Is the instance visible or not", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.rotationAngleProperty(), "Rotation angle", "The rotation angle of the instance", Optional.of(AnglePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.absolutePositionProperty(), "Absolute position", "Is the instances position absolute?", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.absolutePositionAnchorProperty(), "Position anchor", "The position anchor of the instance", Optional.of(AbsolutePositionAnchorPropertyEditor.class)));
        return theResult;
    }
}
