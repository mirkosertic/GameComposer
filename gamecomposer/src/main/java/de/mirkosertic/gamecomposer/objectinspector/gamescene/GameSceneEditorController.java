package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameSceneEditorController implements ChildController {

    static class GameObjectToStringConverter extends StringConverter<GameObject> {
        @Override
        public String toString(GameObject aObject) {
            return aObject.getName();
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

    @Inject
    Event<ObjectUpdatedEvent> objectUpdatedEvent;

    private Parent view;
    private GameScene gameScene;

    public GameSceneEditorController initialize(Parent aView, GameScene aObject) {
        view = aView;
        gameScene = aObject;

        nameTextField.setText(gameScene.getName());

        defaultCamera.getItems().clear();
        defaultCamera.getItems().addAll(aObject.getObjects());
        defaultCamera.setValue(aObject.getCameraObject());
        defaultCamera.setConverter(new GameObjectToStringConverter());

        defaultPlayer.getItems().clear();
        defaultPlayer.getItems().addAll(aObject.getObjects());
        defaultPlayer.setValue(aObject.getDefaultPlayer());
        defaultPlayer.setConverter(new GameObjectToStringConverter());

        backgroundColorPicker.setValue(Color.rgb(aObject.getBackgroundColor().getR(),
                aObject.getBackgroundColor().getG(),
                aObject.getBackgroundColor().getB()));
        backgroundColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Color theColor = backgroundColorPicker.getValue();
                gameScene.setBackgroundColor(new de.mirkosertic.gameengine.core.Color((int) (255 * theColor.getRed()), (int) (255 * theColor.getGreen()), (int) (255 * theColor.getBlue())));
                objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameScene));
            }
        });

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                gameScene.setName(aNewValue);
                objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameScene));
            }
        });

        defaultCamera.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object aOldValue, Object aNewValue) {
                gameScene.setCameraObject((GameObject) aNewValue);
                objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameScene));
            }
        });

        defaultPlayer.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object aOldValue, Object aNewValue) {
                gameScene.setDefaultPlayer((GameObject) aNewValue);
                objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameScene));
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}