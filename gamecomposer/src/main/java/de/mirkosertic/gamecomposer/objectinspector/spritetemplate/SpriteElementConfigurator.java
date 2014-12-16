package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PersistentPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.ResourceNameImagePropertyEditor;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.type.ResourceName;

import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

@ObjectInspectorElementConfiguratorType(clazz = Sprite.class)
public class SpriteElementConfigurator implements ObjectInspectorElementConfigurator<Sprite> {

    private static final String CATEGORY_NAME = "Sprite";

    @Inject
    PersistenceManager persistenceManager;

    @Override
    public List<PropertySheet.Item> getItemsFor(Sprite aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.resourceNameProperty(), "Resource", "The image ressource",
                Optional.of(ResourceNameImagePropertyEditor.class)));
        return theResult;
    }
}