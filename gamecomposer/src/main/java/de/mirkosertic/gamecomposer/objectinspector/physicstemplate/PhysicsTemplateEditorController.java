package de.mirkosertic.gamecomposer.objectinspector.physicstemplate;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class PhysicsTemplateEditorController implements ChildController {

    @FXML
    CheckBox fixedRotation;

    @Inject
    Event<ObjectUpdatedEvent> objectUpdatedEvent;

    private Parent view;
    private PhysicsComponentTemplate object;

    public PhysicsTemplateEditorController initialize(Parent aView, PhysicsComponentTemplate aObject) {
        view = aView;
        object = aObject;

        fixedRotation.setSelected(aObject.isFixedRotation());
        fixedRotation.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aOldValue, Boolean aNewValue) {
                if (aNewValue != null) {
                    object.setFixedRotation(aNewValue);
                    objectUpdatedEvent.fire(new ObjectUpdatedEvent(object.getOwner()));
                }
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}