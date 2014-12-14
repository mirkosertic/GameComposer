package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.sprite.Sprite;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;

@ObjectInspectorElementConfiguratorType(clazz = Sprite.class)
public class SpriteElementConfigurator implements ObjectInspectorElementConfigurator<Sprite> {

    @Override
    public List<PropertySheet.Item> getItemsFor(Sprite aObject) {
        return new ArrayList<>();
    }
}
