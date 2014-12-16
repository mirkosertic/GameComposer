package de.mirkosertic.gamecomposer.objectinspector.texttemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.ColorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.FontPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.TextExpressionPropertyEditor;
import de.mirkosertic.gameengine.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Singleton;
import org.controlsfx.control.PropertySheet;

@Singleton
@ObjectInspectorElementConfiguratorType(clazz = Text.class)
public class TextElementConfigurator implements ObjectInspectorElementConfigurator<Text> {

    private static final String CATEGORY_NAME = "Text";

    @Override
    public List<PropertySheet.Item> getItemsFor(Text aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.textExpressionProperty(), "Text", "The text expression", Optional
                .of(TextExpressionPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.fontProperty(), "Font", "The text font", Optional
                .of(FontPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.colorProperty(), "Color", "The text color", Optional
                .of(ColorPropertyEditor.class)));
        return theResult;
    }
}
