package de.mirkosertic.gamecomposer.objectinspector.eventsheet;

import de.mirkosertic.gamecomposer.objectinspector.EditorContext;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.EventSheet;
import org.controlsfx.control.PropertySheet;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = EventSheet.class)
public class EventSheetElementConfigurator implements ObjectInspectorElementConfigurator<EventSheet> {

    private static final String CATEGORY_NAME = "Event Sheet";

    @Inject
    EditorContext editorContext;

    @Override
    public List<PropertySheet.Item> getItemsFor(EventSheet aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(editorContext, CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the event sheet", Optional.of(StringPropertyEditor.class)));
        return theResult;
    }
}