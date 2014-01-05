package de.mirkosertic.gamecomposer.objectinspector.playerscore;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.playerscore.PlayerScore;
import de.mirkosertic.gameengine.type.ScoreValue;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class PlayerScoreEditorController implements ObjectInspectorElementController {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    TextField score;

    private Parent view;
    private PlayerScore playerScore;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(playerScore.scoreValueProperty());
    }

    public PlayerScoreEditorController initialize(Parent aView, PlayerScore aObject) {
        view = aView;
        playerScore = aObject;

        PropertyBinder.bind(playerScore.scoreValueProperty(), score.textProperty(), new PropertyBinder.Converter<ScoreValue, String>() {
            @Override
            public String beanToUI(ScoreValue aValue) {
                return Long.toString(aValue.score);
            }

            @Override
            public ScoreValue uiToBean(String aValue) {
                Long theScoreValue = Long.valueOf(aValue);
                return new ScoreValue(theScoreValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}
