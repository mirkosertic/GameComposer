package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.function.Consumer;

public class ActionPropertyEditor implements PropertyEditor<Object> {

    private final VBox editor;
    private ActionPropertyEditorItem item;

    public ActionPropertyEditor(PropertySheet.Item aItem) {

        editor = new VBox();
        editor.setAlignment(Pos.BASELINE_LEFT);

        item = (ActionPropertyEditorItem) aItem;
        item.actionStream().forEach(new Consumer<ActionPropertyEditorItem.Action>() {
            @Override
            public void accept(ActionPropertyEditorItem.Action aAction) {
                Hyperlink theHyperlink = new Hyperlink(aAction.getName());
                theHyperlink.setOnAction(aAction.getEventHandler());
                editor.getChildren().add(theHyperlink);
            }
        });
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object aValue) {
    }
}