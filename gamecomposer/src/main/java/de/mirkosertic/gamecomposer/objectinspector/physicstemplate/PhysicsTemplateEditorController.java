package de.mirkosertic.gamecomposer.objectinspector.physicstemplate;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;
import javafx.scene.Node;
import javafx.scene.Parent;

public class PhysicsTemplateEditorController implements ChildController {

    private Parent view;
    private PhysicsComponentTemplate object;

    public PhysicsTemplateEditorController initialize(Parent aView, PhysicsComponentTemplate aObject) {
        view = aView;
        object = aObject;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}