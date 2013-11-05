package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectUpdatedEvent;
import de.mirkosertic.gameengine.core.Game;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameEditorController implements ChildController {

    @FXML
    TextField nameTextField;

    @Inject
    Event<ObjectUpdatedEvent> objectUpdatedEvent;

    private Parent view;
    private Game game;

    public GameEditorController initialize(Parent aView, Game aObject) {
        view = aView;
        game = aObject;

        nameTextField.setText(game.getName());

        nameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                game.setName(aNewValue);
                objectUpdatedEvent.fire(new ObjectUpdatedEvent(game));
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}