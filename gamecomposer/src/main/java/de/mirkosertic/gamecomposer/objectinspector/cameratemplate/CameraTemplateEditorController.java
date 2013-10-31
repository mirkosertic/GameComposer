package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;
import javafx.scene.Node;
import javafx.scene.Parent;

public class CameraTemplateEditorController implements ChildController {

    private Parent view;
    private CameraComponentTemplate object;

    public CameraTemplateEditorController initialize(Parent aView, CameraComponentTemplate aObject) {
        view = aView;
        object = aObject;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}