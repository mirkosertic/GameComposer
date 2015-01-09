package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gameengine.action.SystemTickCondition;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.input.KeyEventCondition;
import de.mirkosertic.gameengine.physic.ObjectCollisionCondition;
import de.mirkosertic.gameengine.process.KillProcessesForInstanceAction;
import de.mirkosertic.gameengine.script.RunScriptAction;
import de.mirkosertic.gameengine.sound.PlaySoundAction;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleEditorController implements Controller {

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
    @Any
    Instance<Object> instances;

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
        knownEventTypes.put(SceneStartedCondition.class.getSimpleName(), SceneStartedCondition.class);
        knownEventTypes.put(KeyEventCondition.class.getSimpleName(), KeyEventCondition.class);
        knownEventTypes.put(ObjectCollisionCondition.class.getSimpleName(), ObjectCollisionCondition.class);
        knownEventTypes.put(GameObjectInstanceAddedToSceneCondition.class.getSimpleName(), GameObjectInstanceAddedToSceneCondition.class);
        knownEventTypes.put(GameObjectInstanceRemovedFromSceneCondition.class.getSimpleName(), GameObjectInstanceRemovedFromSceneCondition.class);
        knownEventTypes.put(SystemTickCondition.class.getSimpleName(), SystemTickCondition.class);
        knownEventTypes.put(GameObjectInstanceLeftLayoutCondition.class.getSimpleName(), GameObjectInstanceLeftLayoutCondition.class);

        knownActions = new ArrayList<>();
        knownActions.add(PlaySoundAction.class);
        knownActions.add(RunSceneAction.class);
        knownActions.add(DeleteGameObjectInstanceAction.class);
        knownActions.add(SpawnGameObjectInstanceAction.class);
        knownActions.add(KillProcessesForInstanceAction.class);
        knownActions.add(RunScriptAction.class);

        setupConditionSelection();
        updateActions();
        updateFilterConditions();

        PropertyBinder.bind(aGameRule.nameProperty(), ruleName.textProperty());

        conditionType.setOnAction(actionEvent -> setConditionType());
        return this;
    }

    private void setupConditionSelection() {
        conditionType.getItems().clear();
        conditionType.getItems().addAll(knownEventTypes.values());
        conditionType.setConverter(new StringConverter<Class>() {
            @Override
            public String toString(Class aClass) {
                return aClass.getSimpleName().replace("Condition", "");
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
            theRemoveActionLink.setOnAction(actionEvent -> {
                gameRule.removeAction(theFinalAction);
                updateActions();
            });
            theActionInfo.getChildren().add(new Label(theAction.getClass().getSimpleName()));
            theActionInfo.getChildren().add(theRemoveActionLink);

            HBox.setHgrow(theActionInfo, Priority.ALWAYS);

            actions.getChildren().add(theActionInfo);

            ActionControllerFactory theFactory = (ActionControllerFactory) instances.select(createActionEditorQualifier(theAction.getClass())).get();
            Controller theController = theFactory.createFor(eventSheet.getGameScene(), gameRule, theAction);
            actions.getChildren().add(theController.getView());
        }
    }

    private ConditionEditorType createConditionEditorQualifier(final Class aClass) {
        return new ConditionEditorType() {
            @Override
            public Class clazz() {
                return aClass;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ConditionEditorType.class;
            }
        };
    }

    private ActionEditorType createActionEditorQualifier(final Class aClass) {
        return new ActionEditorType() {
            @Override
            public Class clazz() {
                return aClass;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ActionEditorType.class;
            }
        };
    }

    private void updateFilterConditions() {
        filterCoditions.getChildren().clear();
        final Condition theCondition = gameRule.conditionProperty().get();
        if (theCondition != null) {
            ConditionControllerFactory theFactory = (ConditionControllerFactory) instances.select(createConditionEditorQualifier(theCondition.getClass())).get();
            Controller theController = theFactory.createFor(eventSheet.getGameScene(), theCondition);
            filterCoditions.getChildren().add(theController.getView());            
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
            MenuItem theItem = new MenuItem(theActionClass.getSimpleName().replace("Action", ""));
            theItem.setOnAction(actionEvent -> {
                try {
                    Action theNewAction = theFinalAction.newInstance();
                    gameRule.addAction(theNewAction);
                    updateActions();
                } catch (Exception e) {
                    throw new RuntimeException(e);
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