package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.ChildController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;

import javax.inject.Singleton;

public class ObjectInspectorController implements ChildController {

    @FXML
    Accordion propertiesAccordion;

    private Node view;

    ObjectInspectorController initialize(Node aView) {
        view = aView;
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}
