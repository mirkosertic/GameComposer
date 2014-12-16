package de.mirkosertic.gamecomposer.objectinspector.constantmovement;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.SpeedPropertyEditor;
import de.mirkosertic.gameengine.arcade.ConstantMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Singleton;
import org.controlsfx.control.PropertySheet;

@Singleton
@ObjectInspectorElementConfiguratorType(clazz = ConstantMovement.class)
public class ConstantMovementElementConfigurator implements ObjectInspectorElementConfigurator<ConstantMovement> {

    private static final String CATEGORY_NAME = "Constant movement";

    @Override
    public List<PropertySheet.Item> getItemsFor(ConstantMovement aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.speedProperty(), "Speed", "The constant speed of the object", Optional
                .of(SpeedPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.rotationSpeedProperty(), "Rotation speed", "The constant rotation speed of the object", Optional
                .of(SpeedPropertyEditor.class)));
        return theResult;
    }
}
