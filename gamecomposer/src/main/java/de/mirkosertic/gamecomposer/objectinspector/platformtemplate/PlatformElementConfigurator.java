package de.mirkosertic.gamecomposer.objectinspector.platformtemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.FloatPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.GameKeyCodePropertyEditor;
import de.mirkosertic.gameengine.physic.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = Platform.class)
public class PlatformElementConfigurator implements ObjectInspectorElementConfigurator<Platform> {

    private static final String CATEGORY_NAME = "Platform";

    @Override
    public List<PropertySheet.Item> getItemsFor(Platform aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.moveLeftKeyProperty(), "Move left key", "The key to perform a move left action", Optional
                .of(GameKeyCodePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.moveRightKeyProperty(), "Move right key", "The key to perform a move right action", Optional
                .of(GameKeyCodePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.jumpKeyProperty(), "Jump key", "The key to perform a jump action", Optional
                .of(GameKeyCodePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.leftRightImpulseProperty(), "Left/Right impulse", "The impulse to apply on left/right action", Optional
                .of(FloatPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.jumpImpulseProperty(), "Jump impulse", "The impulse to apply on jump action", Optional
                .of(FloatPropertyEditor.class)));
        return theResult;
    }
}