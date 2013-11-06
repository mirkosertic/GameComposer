package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.ChildController;
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

    private Parent view;
    private CameraComponentTemplate object;

    public CameraTemplateEditorController initialize(Parent aView, CameraComponentTemplate aObject) {
        view = aView;
        object = aObject;

        cameraType.getItems().clear();
        cameraType.getItems().addAll(Arrays.asList(CameraType.values()));
        cameraType.setValue(object.getType());
        cameraType.setConverter(new CameraTypeToStringConverter());
        cameraType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                object.setType((CameraType) cameraType.getValue());
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}