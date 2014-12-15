package de.mirkosertic.gamecomposer.objectinspector.gameobject;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.SizePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.GameObject;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = GameObject.class)
public class GameObjectElementConfigurator implements ObjectInspectorElementConfigurator<GameObject> {

    private static final String CATEGORY_NAME = "GameObject";

    @Override
    public List<PropertySheet.Item> getItemsFor(GameObject aObject) {
        ArrayList<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the object", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.sizeProperty(), "Size", "The size of the object", Optional.of(SizePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.visibleProperty(), "Visible", "The visibility of the object"));
        return theResult;
    }
}