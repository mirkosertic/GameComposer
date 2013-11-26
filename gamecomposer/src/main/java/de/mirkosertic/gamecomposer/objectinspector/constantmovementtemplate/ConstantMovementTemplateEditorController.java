package de.mirkosertic.gamecomposer.objectinspector.constantmovementtemplate;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.physics.ConstantMovementTemplate;
import de.mirkosertic.gameengine.types.Angle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class ConstantMovementTemplateEditorController implements ObjectInspectorElementController {

    @FXML
    TextField movementDirection;

    @FXML
    TextField movementForce;

    @Inject
    Event<Object> eventGateway;

    private Parent view;
    private ConstantMovementTemplate object;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(object.movementDirectionProperty());
        PropertyBinder.unbind(object.movementForceProperty());
    }

    public ConstantMovementTemplateEditorController initialize(Parent aView, ConstantMovementTemplate aObject) {
        view = aView;
        object = aObject;

        PropertyBinder.bind(object.movementDirectionProperty(), movementDirection.textProperty(), new PropertyBinder.Converter<Angle, String>() {
            @Override
            public String beanToUI(Angle aValue) {
                return Integer.toString(aValue.angleInDegrees);
            }

            @Override
            public Angle uiToBean(String aValue) {
                return new Angle(Integer.parseInt(aValue));
            }
        });
        PropertyBinder.bind(object.movementForceProperty(), movementForce.textProperty(), new PropertyBinder.Converter<Float, String>() {
            @Override
            public String beanToUI(Float aValue) {
                return Float.toString(aValue);
            }

            @Override
            public Float uiToBean(String aValue) {
                return Float.valueOf(aValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}