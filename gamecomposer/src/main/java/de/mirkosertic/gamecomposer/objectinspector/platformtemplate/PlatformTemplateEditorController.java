package de.mirkosertic.gamecomposer.objectinspector.platformtemplate;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;
import javafx.scene.Node;
import javafx.scene.Parent;

public class PlatformTemplateEditorController implements ChildController {

    private Parent view;
    private PlatformComponentTemplate object;

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