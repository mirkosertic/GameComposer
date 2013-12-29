package de.mirkosertic.gamecomposer.objectinspector.platformtemplate;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.physics.Platform;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class PlatformTemplateEditorController implements ObjectInspectorElementController {

    private static class GameKeyCodeConverter extends StringConverter<GameKeyCode> {
        @Override
        public String toString(GameKeyCode aKeyCode) {
            return aKeyCode.name();
        }

        @Override
        public GameKeyCode fromString(String aString) {
            return null;
        }
    }

    @FXML
    ComboBox moveLeftKey;

    @FXML
    ComboBox moveRightKey;

    @FXML
    ComboBox jumpKey;

    @FXML
    TextField leftRightImpulse;

    @FXML
    TextField jumpImpulse;

    private Parent view;
    private Platform object;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(object.moveLeftKeyProperty());
        PropertyBinder.unbind(object.moveRightKeyProperty());
        PropertyBinder.unbind(object.jumpKeyProperty());
        PropertyBinder.unbind(object.leftRightImpulseProperty());
        PropertyBinder.unbind(object.jumpImpulseProperty());
    }

    public PlatformTemplateEditorController initialize(Parent aView, Platform aObject) {
        view = aView;
        object = aObject;

        moveLeftKey.getItems().clear();
        moveLeftKey.getItems().addAll(GameKeyCode.allKeysAsSortedList());
        moveLeftKey.setConverter(new GameKeyCodeConverter());
        moveRightKey.getItems().clear();
        moveRightKey.getItems().addAll(GameKeyCode.allKeysAsSortedList());
        moveRightKey.setConverter(new GameKeyCodeConverter());
        jumpKey.getItems().clear();
        jumpKey.getItems().addAll(GameKeyCode.allKeysAsSortedList());
        jumpKey.setConverter(new GameKeyCodeConverter());

        PropertyBinder.bind(object.moveLeftKeyProperty(), moveLeftKey.valueProperty());
        PropertyBinder.bind(object.moveRightKeyProperty(), moveRightKey.valueProperty());
        PropertyBinder.bind(object.jumpKeyProperty(), jumpKey.valueProperty());
        PropertyBinder.bind(object.leftRightImpulseProperty(), leftRightImpulse.textProperty(), new PropertyBinder.Converter<Float, String>() {
            @Override
            public String beanToUI(Float aValue) {
                return Float.toString(aValue);
            }

            @Override
            public Float uiToBean(String aValue) {
                return Float.parseFloat(aValue);
            }
        });
        PropertyBinder.bind(object.jumpImpulseProperty(), jumpImpulse.textProperty(), new PropertyBinder.Converter<Float, String>() {
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