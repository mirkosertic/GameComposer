package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;

@ObjectInspectorElementConfiguratorType(clazz = GameObjectInstance.class)
public class GameObjectInstanceElementConfigurator implements ObjectInspectorElementConfigurator<GameObjectInstance> {

    @Override
    public List<PropertySheet.Item> getItemsFor(GameObjectInstance aObject) {
        return new ArrayList<>();
    }
}
