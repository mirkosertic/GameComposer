package de.mirkosertic.gamecomposer.objectinspector.physicstemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.physic.Physics;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;

@ObjectInspectorElementConfiguratorType(clazz = Physics.class)
public class PhysicsElementConfigurator implements ObjectInspectorElementConfigurator<Physics> {

    @Override
    public List<PropertySheet.Item> getItemsFor(Physics aObject) {
        return new ArrayList<>();
    }
}
