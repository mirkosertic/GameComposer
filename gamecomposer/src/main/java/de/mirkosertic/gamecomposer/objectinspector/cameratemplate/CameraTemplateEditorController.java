package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.camera.CameraType;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Arrays;

public class CameraTemplateEditorController implements ChildController {

    class CameraTypeToStringConverter extends StringConverter<CameraType> {
        @Override
        public String toString(CameraType aType) {
            return aType.name();
        }

        @Override
        public CameraType fromString(String s) {
            return null;
        }
    }

    @FXML
    ComboBox cameraType;

    @Inject
    Event<Object> eventGateway;

    private Parent view;
    private CameraComponentTemplate object;

    public CameraTemplateEditorController initialize(Parent aView, CameraComponentTemplate aObject) {
        view = aView;
        object = aObject;

        cameraType.getItems().clear();
        cameraType.getItems().addAll(Arrays.asList(CameraType.values()));
        cameraType.setValue(object.typeProperty().get());
        cameraType.setConverter(new CameraTypeToStringConverter());
        cameraType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                object.typeProperty().set((CameraType) cameraType.getValue());
                eventGateway.fire(new ObjectUpdatedEvent(object));
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}