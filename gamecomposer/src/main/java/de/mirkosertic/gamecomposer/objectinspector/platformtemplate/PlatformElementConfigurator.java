package de.mirkosertic.gamecomposer.objectinspector.platformtemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.physic.Platform;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;

@ObjectInspectorElementConfiguratorType(clazz = Platform.class)
public class PlatformElementConfigurator implements ObjectInspectorElementConfigurator<Platform> {

    @Override
    public List<PropertySheet.Item> getItemsFor(Platform aObject) {
        return new ArrayList<>();
    }
}
