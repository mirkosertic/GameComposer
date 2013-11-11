package de.mirkosertic.gamecomposer.objectinspector.physicstemplate;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorChildController;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;

public class PhysicsTemplateEditorController implements ObjectInspectorChildController {

    @FXML
    CheckBox fixedRotation;

    private Parent view;
    private PhysicsComponentTemplate object;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(object.fixedRotationProperty());
    }

    public PhysicsTemplateEditorController initialize(Parent aView, PhysicsComponentTemplate aObject) {
        view = aView;
        object = aObject;

        PropertyBinder.bind(object.fixedRotationProperty(), fixedRotation.selectedProperty());

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}