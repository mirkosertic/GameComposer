package de.mirkosertic.gamecomposer.objectinspector.texttemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.text.Text;
import org.controlsfx.control.PropertySheet;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@ObjectInspectorElementConfiguratorType(clazz = Text.class)
public class TextElementConfigurator implements ObjectInspectorElementConfigurator<Text> {

    @Override
    public List<PropertySheet.Item> getItemsFor(Text aObject) {
        return new ArrayList<>();
    }
}
