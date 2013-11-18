package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition.KeyEventConditionEditorController;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition.KeyEventConditionEditorControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty.SetPropertyEditorController;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty.SetPropertyEditorControllerFactory;
import de.mirkosertic.gameengine.action.SetPropertyAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gameengine.action.PlaySoundAction;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound.PlaySoundEditorController;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound.PlaySoundEditorControllerFactory;

import javax.inject.Inject;

public class RuleEditorController implements ChildController {

    @FXML
    TextField ruleName;

    @FXML
    ComboBox conditionType;

    @FXML
    VBox filterCoditions;

    @FXML
    Hyperlink addNewAction;

    @FXML
    VBox actions;

    @Inject
    PlaySoundEditorControllerFactory playSoundEditorControllerFactory;

    @Inject
    SetPropertyEditorControllerFactory setGameObjectInstancePropertyEditorFactory;

    @Inject
    KeyEventConditionEditorControllerFactory keyEventConditionEditorControllerFactory;

    private Node view;
    private GameRule gameRule;
    private EventSheet eventSheet;
    private EventSheetEditorController parentController;

    private Map<String, Class> knownEventTypes;
    private List<Class<? extends Action>> knownActions;

    RuleEditorController initialize(EventSheetEditorController aParentController, BorderPane aView,
                                    EventSheet aEventSheet, GameRule aGameRule) {
        eventSheet = aEventSheet;
        view = aView;
        parentController = aParentController;
        gameRule = aGameRule;

        knownEventTypes = new HashMap<>();
        knownEventTypes.put(KeyEventCondition.class.getSimpleName(), KeyEventCondition.class);

        knownActions = new ArrayList<>();
        knownActions.add(PlaySoundAction.class);
        knownActions.add(SetPropertyAction.class);

        setupConditionSelection();
        updateActions();
        updateFilterConditions();

        PropertyBinder.bind(aGameRule.nameProperty(), ruleName.textProperty());

        conditionType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setConditionType();
            }
        });
        return this;
    }

    private void setupConditionSelection() {
        conditionType.getItems().clear();
        conditionType.getItems().addAll(knownEventTypes.values());
        conditionType.setConverter(new StringConverter<Class>() {
            @Override
            public String toString(Class aClass) {
                return aClass.getSimpleName();
            }

            @Override
            public Class fromString(String s) {
                return null;
            }
        });

        if (!gameRule.conditionProperty().isNull()) {
            conditionType.setValue(gameRule.conditionProperty().get().getClass());
        }
    }

    private void setConditionType() {
        Class theEventType = (Class) conditionType.valueProperty().get();
        try {
            Condition theCondition = (Condition) theEventType.newInstance();
            gameRule.conditionProperty().set(theCondition);

            updateFilterConditions();
            updateActions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateActions() {
        actions.getChildren().clear();
        for (Action theAction : gameRule.getActions()) {

            // Name of the Action and a Remove
            VBox theActionInfo = new VBox();
            final Action theFinalAction = theAction;
            Hyperlink theRemoveActionLink = new Hyperlink("Remove");
            theRemoveActionLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    gameRule.removeAction(theFinalAction);
                    updateActions();
                }
            });
            theActionInfo.getChildren().add(new Label(theAction.getClass().getSimpleName()));
            theActionInfo.getChildren().add(theRemoveActionLink);

            HBox.setHgrow(theActionInfo, Priority.ALWAYS);

            actions.getChildren().add(theActionInfo);

            if (theAction instanceof PlaySoundAction) {
                PlaySoundEditorController theController = playSoundEditorControllerFactory.createFor(eventSheet.getGameScene(), (PlaySoundAction) theAction);
                actions.getChildren().add(theController.getView());
            }
            if (theAction instanceof SetPropertyAction) {
                SetPropertyEditorController theController = setGameObjectInstancePropertyEditorFactory.createFor(eventSheet.getGameScene(), (SetPropertyAction) theAction);
                actions.getChildren().add(theController.getView());
            }
        }
    }

    private void updateFilterConditions() {
        filterCoditions.getChildren().clear();
        final Condition theCondition = gameRule.conditionProperty().get();
        if (theCondition != null) {
            if (theCondition instanceof KeyEventCondition) {

                KeyEventConditionEditorController theController = keyEventConditionEditorControllerFactory.createFor(eventSheet.getGameScene(), (KeyEventCondition) theCondition);
                filterCoditions.getChildren().add(theController.getView());
            }
        }
    }

    @Override
    public Node getView() {
        return view;
    }

    @FXML
    public void onRemoveRule() {
        eventSheet.removeRule(gameRule);
        parentController.initializeRuleList();
    }

    @FXML
    public void onAddNewAction() {
        ContextMenu theContextMenu = new ContextMenu();

        for (Class<? extends Action> theActionClass : knownActions) {
            final Class<? extends Action> theFinalAction = theActionClass;
            MenuItem theItem = new MenuItem(theActionClass.getSimpleName());
            theItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        Action theNewAction = theFinalAction.newInstance();
                        gameRule.addAction(theNewAction);
                        updateActions();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            theContextMenu.getItems().add(theItem);
        }
        theContextMenu.show(addNewAction, Side.BOTTOM, 0, 0);
    }

    public void removed() {
        PropertyBinder.unbind(gameRule.nameProperty());
    }
}
