package de.mirkosertic.gamecomposer.objectinspector.physicstemplate;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.physics.Physics;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class PhysicsTemplateEditorController implements ObjectInspectorElementController {

    @FXML
    CheckBox active;

    @FXML
    CheckBox fixedRotation;

    @FXML
    TextField density;

    @FXML
    TextField friction;

    @FXML
    TextField restitution;

    @FXML
    TextField gravityScale;

    private Parent view;
    private Physics object;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(object.activeProperty());
        PropertyBinder.unbind(object.fixedRotationProperty());
        PropertyBinder.unbind(object.densityProperty());
        PropertyBinder.unbind(object.frictionProperty());
        PropertyBinder.unbind(object.restitutionProperty());
        PropertyBinder.unbind(object.gravityScaleProperty());
    }

    public PhysicsTemplateEditorController initialize(Parent aView, Physics aObject) {
        view = aView;
        object = aObject;

        PropertyBinder.bind(object.activeProperty(), active.selectedProperty());
        PropertyBinder.bind(object.fixedRotationProperty(), fixedRotation.selectedProperty());
        PropertyBinder.bind(object.densityProperty(), density.textProperty(), new PropertyBinder.Converter<Float, String>() {
            @Override
            public String beanToUI(Float aValue) {
                return Float.toString(aValue);
            }

            @Override
            public Float uiToBean(String aValue) {
                return Float.parseFloat(aValue);
            }
        });
        PropertyBinder.bind(object.frictionProperty(), friction.textProperty(), new PropertyBinder.Converter<Float, String>() {
            @Override
            public String beanToUI(Float aValue) {
                return Float.toString(aValue);
            }

            @Override
            public Float uiToBean(String aValue) {
                return Float.parseFloat(aValue);
            }
        });
        PropertyBinder.bind(object.restitutionProperty(), restitution.textProperty(), new PropertyBinder.Converter<Float, String>() {
            @Override
            public String beanToUI(Float aValue) {
                return Float.toString(aValue);
            }

            @Override
            public Float uiToBean(String aValue) {
                return Float.parseFloat(aValue);
            }
        });
        PropertyBinder.bind(object.gravityScaleProperty(), gravityScale.textProperty(), new PropertyBinder.Converter<Float, String>() {
            @Override
            public String beanToUI(Float aValue) {
                return Float.toString(aValue);
            }

            @Override
            public Float uiToBean(String aValue) {
                return Float.parseFloat(aValue);
            }
        });


        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}