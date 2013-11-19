package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;

public class GameSceneEditorController implements ObjectInspectorElementController {

    private static class GameObjectToStringConverter extends StringConverter<GameObject> {
        @Override
        public String toString(GameObject aObject) {
            return aObject.nameProperty().get();
        }

        @Override
        public GameObject fromString(String s) {
            return null;
        }
    }

    @FXML
    TextField nameTextField;

    @FXML
    ComboBox defaultCamera;

    @FXML
    ComboBox defaultPlayer;

    @FXML
    ColorPicker backgroundColorPicker;

    private Parent view;
    private GameScene gameScene;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(gameScene.nameProperty());
        PropertyBinder.unbind(gameScene.cameraObjectProperty());
        PropertyBinder.unbind(gameScene.defaultPlayerProperty());
        PropertyBinder.unbind(gameScene.backgroundColorProperty());
    }

    public GameSceneEditorController initialize(Parent aView, GameScene aObject) {
        view = aView;
        gameScene = aObject;

        PropertyBinder.bind(aObject.nameProperty(), nameTextField.textProperty());

        defaultCamera.getItems().clear();
        defaultCamera.getItems().addAll(aObject.getObjects());
        defaultCamera.setConverter(new GameObjectToStringConverter());
        PropertyBinder.bind(aObject.cameraObjectProperty(), defaultCamera.valueProperty());

        defaultPlayer.getItems().clear();
        defaultPlayer.getItems().addAll(aObject.getObjects());
        defaultPlayer.setConverter(new GameObjectToStringConverter());
        PropertyBinder.bind(aObject.defaultPlayerProperty(), defaultPlayer.valueProperty());

        PropertyBinder.bind(aObject.backgroundColorProperty(), backgroundColorPicker.valueProperty(),
                new PropertyBinder.Converter<de.mirkosertic.gameengine.types.Color, Color>() {
                    @Override
                    public Color beanToUI(de.mirkosertic.gameengine.types.Color aValue) {
                        return Color.rgb(aValue.r, aValue.g, aValue.b);
                    }

                    @Override
                    public de.mirkosertic.gameengine.types.Color uiToBean(Color aValue) {
                        return new de.mirkosertic.gameengine.types.Color((int) (aValue.getRed() * 255), (int) (aValue
                                .getGreen() * 255), (int) (aValue.getBlue() * 255));
                    }
                });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}
