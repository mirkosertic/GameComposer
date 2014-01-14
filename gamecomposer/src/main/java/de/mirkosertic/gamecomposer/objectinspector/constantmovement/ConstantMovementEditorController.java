package de.mirkosertic.gamecomposer.objectinspector.constantmovement;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.arcade.ConstantMovement;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Speed;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class ConstantMovementEditorController implements ObjectInspectorElementController {

    @FXML
    TextField angleTextField;

    @FXML
    TextField speedTextField;

    @Inject
    StringConverterFactory stringConverterFactory;

    private Parent view;
    private ConstantMovement constantMovement;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(constantMovement.angleProperty());
        PropertyBinder.unbind(constantMovement.speedProperty());
    }

    public ConstantMovementEditorController initialize(Parent aView, ConstantMovement aObject) {
        view = aView;
        constantMovement = aObject;

        PropertyBinder.bind(constantMovement.angleProperty(), angleTextField.textProperty(), new PropertyBinder.Converter<Angle, String>() {
            @Override
            public String beanToUI(Angle aValue) {
                return Integer.toString(aValue.angleInDegrees);
            }

            @Override
            public Angle uiToBean(String aValue) {
                return new Angle(Integer.parseInt(aValue));
            }
        });

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


        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}