package de.mirkosertic.gamecomposer.objectinspector.platformtemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorChildController;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;

import javafx.scene.Node;
import javafx.scene.Parent;

public class PlatformTemplateEditorController implements ObjectInspectorChildController {

    private Parent view;
    private PlatformComponentTemplate object;

    @Override
    public void cleanup() {
    }

    public PlatformTemplateEditorController initialize(Parent aView, PlatformComponentTemplate aObject) {
        view = aView;
        object = aObject;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}