package de.mirkosertic.gamecomposer.contentarea.eventsheet.constantmovement;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.arcade.ConstantMovementAction;
import de.mirkosertic.gameengine.type.Angle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ConstantMovementEditorController implements ActionController {

    @FXML
    TextField movementDirection;

    @FXML
    TextField movementForce;

    private Node view;

    ConstantMovementEditorController initialize(BorderPane aRoot, ConstantMovementAction aAction) {
        view = aRoot;

        PropertyBinder.bindUIToBean(aAction.movementDirectionProperty(), movementDirection.textProperty(), new PropertyBinder.Converter<Angle, String>() {
            @Override
            public String beanToUI(Angle aValue) {
                return Integer.toString(aValue.angleInDegrees);
            }

            @Override
            public Angle uiToBean(String aValue) {
                return new Angle(Integer.valueOf(aValue));
            }
        });
        PropertyBinder.bindUIToBean(aAction.movementForceProperty(), movementForce.textProperty(), new PropertyBinder.Converter<Float, String>() {
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
