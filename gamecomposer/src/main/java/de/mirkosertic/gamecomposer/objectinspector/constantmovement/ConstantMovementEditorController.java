package de.mirkosertic.gamecomposer.objectinspector.constantmovement;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.arcade.ConstantMovement;
import de.mirkosertic.gameengine.type.Speed;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class ConstantMovementEditorController implements ObjectInspectorElementController {

    @FXML
    TextField speedTextField;

    @FXML
    TextField rotationSpeedTextField;

    @Inject
    StringConverterFactory stringConverterFactory;

    private Parent view;
    private ConstantMovement constantMovement;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(constantMovement.speedProperty());
        PropertyBinder.unbind(constantMovement.rotationSpeedProperty());
    }

    public ConstantMovementEditorController initialize(Parent aView, ConstantMovement aObject) {
        view = aView;
        constantMovement = aObject;

        PropertyBinder.bind(constantMovement.speedProperty(), speedTextField.textProperty(), new PropertyBinder.Converter<Speed, String>() {
            @Override
            public String beanToUI(Speed aValue) {
                return Long.toString(aValue.speed);
            }

            @Override
            public Speed uiToBean(String aValue) {
                return new Speed(Long.parseLong(aValue));
            }
        });

        PropertyBinder.bind(constantMovement.rotationSpeedProperty(), rotationSpeedTextField.textProperty(), new PropertyBinder.Converter<Speed, String>() {
            @Override
            public String beanToUI(Speed aValue) {
                return Long.toString(aValue.speed);
            }

            @Override
            public Speed uiToBean(String aValue) {
                return new Speed(Long.parseLong(aValue));
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}