package de.mirkosertic.gamecomposer.objectinspector.statictemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;

import javafx.scene.Node;
import javafx.scene.Parent;

public class StaticTemplateEditorController implements ObjectInspectorElementController {

    private Parent view;
    private StaticComponentTemplate object;

    @Override
    public void cleanup() {
    }

    public StaticTemplateEditorController initialize(Parent aView, StaticComponentTemplate aObject) {
        view = aView;
        object = aObject;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}