package de.mirkosertic.gamecomposer.contentarea.eventsheet.spawninstance;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.core.SpawnGameObjectInstanceAction;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.types.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpawnIstanceEditorController implements Controller {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    ComboBox referenceGameObject;

    @FXML
    ComboBox gameObject;

    @FXML
    TextField offsetX;

    @FXML
    TextField offsetY;

    @Inject
    PersistenceManager persistenceManager;

    private Node view;
    private SpawnGameObjectInstanceAction action;

    SpawnIstanceEditorController initialize(GameScene aGameScene, Node aView, SpawnGameObjectInstanceAction aAction) {
        view = aView;
        action = aAction;

        gameObject.getItems().clear();
        gameObject.setConverter(stringConverterFactory.createGameObjectStringConverter());
        referenceGameObject.getItems().clear();
        referenceGameObject.setConverter(stringConverterFactory.createGameObjectStringConverter());

        List<GameObject> theSortedGameObjects = Arrays.asList(aGameScene.getObjects());
        Collections.sort(theSortedGameObjects, new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                return o1.nameProperty().get().compareTo(o2.nameProperty().get());
            }
        });

        gameObject.getItems().addAll(theSortedGameObjects);
        referenceGameObject.getItems().addAll(theSortedGameObjects);
        gameObject.getSelectionModel().select(action.gameObjectProperty().get());
        referenceGameObject.getSelectionModel().select(action.referenceObjectProperty().get());

        PropertyBinder.bindUIToBean(action.offsetProperty(), offsetX.textProperty(), new PropertyBinder.Converter<Position, String>() {
            @Override
            public String beanToUI(Position aValue) {
                return Integer.toString((int) aValue.x);
            }

            @Override
            public Position uiToBean(String aValue) {
                Position theCurrentPosition = action.offsetProperty().get();
                return new Position(Float.parseFloat(aValue), theCurrentPosition.y);
            }
        });
        PropertyBinder.bindUIToBean(action.offsetProperty(), offsetY.textProperty(), new PropertyBinder.Converter<Position, String>() {
            @Override
            public String beanToUI(Position aValue) {
                return Integer.toString((int) aValue.y);
            }

            @Override
            public Position uiToBean(String aValue) {
                Position theCurrentPosition = action.offsetProperty().get();
                return new Position(theCurrentPosition.x, Float.parseFloat(aValue));
            }
        });

        return this;
    }

    public Node getView() {
        return view;
    }

    @FXML
    public void onSetGameObject() {
        action.gameObjectProperty().set((GameObject) gameObject.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void onSetGameObjectReference() {
        action.gameObjectProperty().set((GameObject) referenceGameObject.getSelectionModel().getSelectedItem());
    }
}