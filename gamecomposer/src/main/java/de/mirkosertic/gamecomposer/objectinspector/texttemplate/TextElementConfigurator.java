package de.mirkosertic.gamecomposer.objectinspector.texttemplate;

import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.ColorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.FontPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.TextExpressionPropertyEditor;
import de.mirkosertic.gameengine.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = Text.class)
public class TextElementConfigurator implements ObjectInspectorElementConfigurator<Text> {

    private static final String CATEGORY_NAME = "80 Text";

    @Inject
    MessageBox messageBox;

    @Override
    public List<PropertySheet.Item> getItemsFor(Text aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.isScriptProperty(), "Is Script", "Is the text expression a script?"));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.textExpressionProperty(), "Text", "The text expression", Optional
                .of(TextExpressionPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.fontProperty(), "Font", "The text font", Optional
                .of(FontPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.colorProperty(), "Color", "The text color", Optional
                .of(ColorPropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete behavior...", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete behavior", "Do you really want to delete this behavior?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                    aObject.delete();
                }
            }
        }));
        theResult.add(theActions);

        return theResult;
    }
}