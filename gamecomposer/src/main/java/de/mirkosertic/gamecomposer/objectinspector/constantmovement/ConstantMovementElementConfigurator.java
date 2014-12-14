package de.mirkosertic.gamecomposer.objectinspector.constantmovement;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.arcade.ConstantMovement;
import org.controlsfx.control.PropertySheet;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@ObjectInspectorElementConfiguratorType(clazz = ConstantMovement.class)
public class ConstantMovementElementConfigurator implements ObjectInspectorElementConfigurator<ConstantMovement> {

    @Override
    public List<PropertySheet.Item> getItemsFor(ConstantMovement aObject) {
        return new ArrayList<>();
    }
}
