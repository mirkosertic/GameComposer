package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import javafx.scene.Node;
import javafx.scene.Parent;

public class SpriteTemplateEditorController implements ChildController {

    private Parent view;
    private SpriteComponentTemplate object;

    public SpriteTemplateEditorController initialize(Parent aView, SpriteComponentTemplate aObject) {
        view = aView;
        object = aObject;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}