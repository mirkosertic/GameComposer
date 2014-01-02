package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.types.Rectangle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class GameSceneEditorController implements ObjectInspectorElementController {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    TextField nameTextField;

    @FXML
    ComboBox defaultCamera;

    @FXML
    ComboBox defaultPlayer;

    @FXML
    ColorPicker backgroundColorPicker;

    @FXML
    TextField layoutBoundsX;

    @FXML
    TextField layoutBoundsY;

    @FXML
    TextField layoutBoundsWidth;

    @FXML
    TextField layoutBoundsHeight;

    private Parent view;
    private GameScene gameScene;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(gameScene.nameProperty());
        PropertyBinder.unbind(gameScene.cameraObjectProperty());
        PropertyBinder.unbind(gameScene.defaultPlayerProperty());
        PropertyBinder.unbind(gameScene.backgroundColorProperty());
        PropertyBinder.unbind(gameScene.layoutBoundsProperty());
    }

    public GameSceneEditorController initialize(Parent aView, GameScene aObject) {
        view = aView;
        gameScene = aObject;

        PropertyBinder.bind(aObject.nameProperty(), nameTextField.textProperty());

        defaultCamera.getItems().clear();
        defaultCamera.getItems().addAll(aObject.getObjects());
        defaultCamera.setConverter(stringConverterFactory.createGameObjectStringConverter());
        PropertyBinder.bind(aObject.cameraObjectProperty(), defaultCamera.valueProperty());

        defaultPlayer.getItems().clear();
        defaultPlayer.getItems().addAll(aObject.getObjects());
        defaultPlayer.setConverter(stringConverterFactory.createGameObjectStringConverter());
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

        PropertyBinder.bind(aObject.layoutBoundsProperty(), layoutBoundsX.textProperty(), new PropertyBinder.Converter<Rectangle, String>() {
            @Override
            public String beanToUI(Rectangle aValue) {
                return Integer.toString((int) aValue.position.x);
            }

            @Override
            public Rectangle uiToBean(String aValue) {
                Float theValue = Float.valueOf(aValue);
                Rectangle theCurrentRectangle = gameScene.layoutBoundsProperty().get();
                return theCurrentRectangle.newPosition(theCurrentRectangle.position.changeX(theValue));
            }
        });
        PropertyBinder.bind(aObject.layoutBoundsProperty(), layoutBoundsY.textProperty(), new PropertyBinder.Converter<Rectangle, String>() {
            @Override
            public String beanToUI(Rectangle aValue) {
                return Integer.toString((int) aValue.position.y);
            }

            @Override
            public Rectangle uiToBean(String aValue) {
                Float theValue = Float.valueOf(aValue);
                Rectangle theCurrentRectangle = gameScene.layoutBoundsProperty().get();
                return theCurrentRectangle.newPosition(theCurrentRectangle.position.changeY(theValue));
            }
        });
        PropertyBinder.bind(aObject.layoutBoundsProperty(), layoutBoundsWidth.textProperty(), new PropertyBinder.Converter<Rectangle, String>() {
            @Override
            public String beanToUI(Rectangle aValue) {
                return Integer.toString(aValue.size.width);
            }

            @Override
            public Rectangle uiToBean(String aValue) {
                int theValue = Integer.valueOf(aValue);
                Rectangle theCurrentRectangle = gameScene.layoutBoundsProperty().get();
                return theCurrentRectangle.newSize(theCurrentRectangle.size.changeWidth(theValue));
            }
        });
        PropertyBinder.bind(aObject.layoutBoundsProperty(), layoutBoundsHeight.textProperty(), new PropertyBinder.Converter<Rectangle, String>() {
            @Override
            public String beanToUI(Rectangle aValue) {
                return Integer.toString(aValue.size.height);
            }

            @Override
            public Rectangle uiToBean(String aValue) {
                int theValue = Integer.valueOf(aValue);
                Rectangle theCurrentRectangle = gameScene.layoutBoundsProperty().get();
                return theCurrentRectangle.newSize(theCurrentRectangle.size.changeHeight(theValue));
            }
        });


        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}
