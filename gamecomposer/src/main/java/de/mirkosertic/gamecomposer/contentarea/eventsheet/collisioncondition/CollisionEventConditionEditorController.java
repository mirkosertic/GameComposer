package de.mirkosertic.gamecomposer.contentarea.eventsheet.collisioncondition;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.physics.ObjectCollisionCondition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollisionEventConditionEditorController implements Controller {

    @FXML
    ComboBox primaryObject;

    @FXML
    ComboBox secondaryObject;

    private GameScene gameScene;
    private ObjectCollisionCondition condition;
    private Node view;

    CollisionEventConditionEditorController initialize(Node aView, GameScene aGameScene, ObjectCollisionCondition aCondition) {
        gameScene = aGameScene;
        view = aView;
        condition = aCondition;

        List<GameObject> theSortedGameObjects = Arrays.asList(aGameScene.getObjects());
        Collections.sort(theSortedGameObjects, new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                return o1.nameProperty().get().compareTo(o2.nameProperty().get());
            }
        });


        primaryObject.getItems().clear();
        primaryObject.getItems().addAll(theSortedGameObjects);
        primaryObject.setConverter(new StringConverter<GameObject>() {
            @Override
            public String toString(GameObject aValue) {
                return aValue.nameProperty().get();
            }

            @Override
            public GameObject fromString(String aValue) {
                // Nonsense here
                return null;
            }
        });
        primaryObject.getSelectionModel().select(aCondition.primaryObjectProperty().get());

        secondaryObject.getItems().clear();
        secondaryObject.getItems().addAll(theSortedGameObjects);
        secondaryObject.setConverter(new StringConverter<GameObject>() {
            @Override
            public String toString(GameObject aValue) {
                return aValue.nameProperty().get();
            }

            @Override
            public GameObject fromString(String aValue) {
                // Nonsense here
                return null;
            }
        });
        secondaryObject.getSelectionModel().select(aCondition.secondaryObjectProperty().get());

        primaryObject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onPrimarySelected();
            }
        });
        secondaryObject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onSecondarySelected();
            }
        });

        return this;
    }

    public Node getView() {
        return view;
    }

    private void onPrimarySelected() {
        condition.primaryObjectProperty().set((GameObject) primaryObject.getSelectionModel().getSelectedItem());
    }

    private void onSecondarySelected() {
        condition.secondaryObjectProperty().set((GameObject) secondaryObject.getSelectionModel().getSelectedItem());
    }
}
