package de.mirkosertic.gamecomposer.objectinspector.statictemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.physic.Static;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;

@ObjectInspectorElementConfiguratorType(clazz = Static.class)
public class StaticElementConfigurator implements ObjectInspectorElementConfigurator<Static> {

    @Override
    public List<PropertySheet.Item> getItemsFor(Static aObject) {
        return new ArrayList<>();
    }
}
