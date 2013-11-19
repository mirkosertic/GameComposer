package de.mirkosertic.gamecomposer.objectinspector.eventsheet;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gameengine.core.EventSheet;

public class EventSheetEditorController implements ObjectInspectorElementController {

    @FXML
    TextField nameTextField;

    private Parent view;
    private EventSheet eventSheet;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(eventSheet.nameProperty());
    }

    public EventSheetEditorController initialize(Parent aView, EventSheet aObject) {
        view = aView;
        eventSheet = aObject;

        PropertyBinder.bind(aObject.nameProperty(), nameTextField.textProperty());

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}
