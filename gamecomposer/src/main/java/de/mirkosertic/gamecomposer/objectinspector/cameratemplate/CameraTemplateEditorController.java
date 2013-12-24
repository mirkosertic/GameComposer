package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.Arrays;

public class CameraTemplateEditorController implements ObjectInspectorElementController {

    private class CameraTypeToStringConverter extends StringConverter<CameraType> {
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
    private Camera object;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(object.typeProperty());
    }

    public CameraTemplateEditorController initialize(Parent aView, Camera aObject) {
        view = aView;
        object = aObject;

        cameraType.getItems().clear();
        cameraType.getItems().addAll(Arrays.asList(CameraType.values()));
        cameraType.setConverter(new CameraTypeToStringConverter());

        PropertyBinder.bind(aObject.typeProperty(), cameraType.valueProperty());

        cameraType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                object.typeProperty().set((CameraType) cameraType.getValue());
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}