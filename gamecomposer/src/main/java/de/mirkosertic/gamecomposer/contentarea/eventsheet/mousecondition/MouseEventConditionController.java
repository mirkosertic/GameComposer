package de.mirkosertic.gamecomposer.contentarea.eventsheet.mousecondition;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.input.MouseEventCondition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class MouseEventConditionController implements Controller {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    ComboBox eventType;

    @FXML
    CheckBox ignoreInstances;

    @FXML
    ComboBox objectType;

    private GameScene gameScene;
    private MouseEventCondition condition;
    private Node view;

    MouseEventConditionController initialize(Node aView, GameScene aGameScene, MouseEventCondition aCondition) {
        gameScene = aGameScene;
        view = aView;
        condition = aCondition;

        List<GameObject> theSortedGameObjects = Arrays.asList(aGameScene.getObjects());
        Collections.sort(theSortedGameObjects, (o1, o2) -> o1.nameProperty().get().compareTo(o2.nameProperty().get()));

        eventType.getItems().clear();
        eventType.getItems().addAll(MouseEventCondition.MouseEventType.values());
        eventType.setConverter(stringConverterFactory.createEnumStringConverter());

        ignoreInstances.setSelected(true);

        objectType.getItems().clear();
        objectType.getItems().addAll(theSortedGameObjects);
        objectType.setConverter(stringConverterFactory.createGameObjectStringConverter());

        return this;
    }

    public void setEventType() {
    }

    public void setFilterInstances() {
    }

    public void setObjectType() {
    }

    @Override
    public Node getView() {
        return view;
    }
}
