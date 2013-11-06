package de.mirkosertic.gamecomposer.objectinspector.gameobject;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.Size;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectEditorController implements ChildController {

    @FXML
    TextField uuidTextField;

    @FXML
    TextField nameTextField;

    @FXML
    TextField widthTextField;

    @FXML
    TextField heightTextField;

    @Inject
    Event<ObjectUpdatedEvent> objectUpdatedEvent;

    @Inject
    Event<ObjectSelectedEvent> objectSelectedEvent;

    @FXML
    VBox componentAddHyperlinks;

    private Parent view;
    private GameObject gameObject;

    public GameObjectEditorController initialize(Parent aView, GameObject aObject) {
        view = aView;
        gameObject = aObject;

        uuidTextField.setText(gameObject.getUuid());
        nameTextField.setText(gameObject.getName());
        widthTextField.setText(Integer.toString(aObject.getSize().width));
        heightTextField.setText(Integer.toString(aObject.getSize().height));

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                gameObject.setName(aNewValue);
            }
        });
        widthTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Size theSize = gameObject.getSize();
                    gameObject.getGameScene().updateObjectSize(gameObject, new Size(Integer.valueOf(aNewValue), theSize.height));
                    objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameObject));
                }
            }
        });
        heightTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                if (!StringUtils.isEmpty(aNewValue)) {
                    Size theSize = gameObject.getSize();
                    gameObject.getGameScene().updateObjectSize(gameObject, new Size(theSize.width, Integer.valueOf(aNewValue)));
                    objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameObject));
                }
            }
        });

        Map<Class<? extends GameComponentTemplate>, String> theKnownTemplates = new HashMap<>();
        theKnownTemplates.put(CameraComponentTemplate.class, "CameraComponent");
        theKnownTemplates.put(StaticComponentTemplate.class, "StaticComponent");
        theKnownTemplates.put(SpriteComponentTemplate.class, "SpriteComponent");
        theKnownTemplates.put(PhysicsComponentTemplate.class, "PhysicsComponent");
        theKnownTemplates.put(PlatformComponentTemplate.class, "PlatformComponent");
        for (Map.Entry<Class<? extends GameComponentTemplate>, String> theEntry : theKnownTemplates.entrySet()) {
            if (aObject.getComponentTemplate(theEntry.getKey()) == null) {

                final Class<? extends GameComponentTemplate> theFinalClass = theEntry.getKey();
                Hyperlink theHyperlink = new Hyperlink();
                theHyperlink.setText("Add "+theEntry.getValue());
                theHyperlink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        addComponent(theFinalClass);
                    }
                });
                componentAddHyperlinks.getChildren().add(theHyperlink);
            }
        }

        return this;
    }

    private void addComponent(Class<? extends GameComponentTemplate> aClass) {
        try {
            GameComponentTemplate theTemplate = aClass.getConstructor(GameObject.class).newInstance(gameObject);
            gameObject.add(theTemplate);

            objectUpdatedEvent.fire(new ObjectUpdatedEvent(gameObject));
            objectSelectedEvent.fire(new ObjectSelectedEvent(gameObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Node getView() {
        return view;
    }
}