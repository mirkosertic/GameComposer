package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gameengine.action.PlaySoundAction;
import de.mirkosertic.gameengine.core.KeyReleased;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.physics.GameObjectCollision;
import de.mirkosertic.gameengine.action.SetGameObjectInstancePropertyAction;
import de.mirkosertic.gameengine.action.SetGameObjectPropertyAction;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound.PlaySoundEditorController;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound.PlaySoundEditorControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectinstanceproperty.SetGameObjectInstancePropertyEditorController;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectinstanceproperty.SetGameObjectInstancePropertyEditorControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectproperty.SetGameObjectPropertyEditorController;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectproperty.SetGameObjectPropertyEditorControllerFactory;


import javax.inject.Inject;

public class RuleEditorController implements ChildController {

    @FXML
    TextField ruleName;

    @FXML
    ComboBox eventType;

    @FXML
    VBox filterCoditions;

    @FXML
    Hyperlink addNewAction;

    @FXML
    HBox actions;

    @Inject
    ControlFactory controlFactory;

    @Inject
    PlaySoundEditorControllerFactory playSoundEditorControllerFactory;

    @Inject
    SetGameObjectInstancePropertyEditorControllerFactory setGameObjectInstancePropertyEditorFactory;

    @Inject
    SetGameObjectPropertyEditorControllerFactory setGameObjectPropertyEditorControllerFactory;

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
        knownEventTypes.put(GameLoopRun.class.getSimpleName(), GameLoopRun.class);
        knownEventTypes.put(KeyPressed.class.getSimpleName(), KeyPressed.class);
        knownEventTypes.put(KeyReleased.class.getSimpleName(), KeyReleased.class);
        knownEventTypes.put(GameObjectCollision.class.getSimpleName(), GameObjectCollision.class);

        knownActions = new ArrayList<>();
        knownActions.add(PlaySoundAction.class);
        knownActions.add(SetGameObjectPropertyAction.class);
        knownActions.add(SetGameObjectInstancePropertyAction.class);

        setupEventSelection();
        updateActions();
        updateFilterConditions();

        PropertyBinder.bind(aGameRule.nameProperty(), ruleName.textProperty());

        eventType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setEventType();
            }
        });
        return this;
    }

    private void setupEventSelection() {
        eventType.getItems().clear();
        eventType.getItems().addAll(knownEventTypes.values());
        eventType.setConverter(new StringConverter<Class>() {
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
            MatchEventCondition theCondition = (MatchEventCondition) gameRule.conditionProperty().get();
            eventType.setValue(knownEventTypes.get(theCondition.eventTypeProperty().get()));
        }
    }

    private void setEventType() {
        Class theEventType = (Class) eventType.valueProperty().get();
        MatchEventCondition theMatchEventCondition = new MatchEventCondition();
        theMatchEventCondition.eventTypeProperty().set(theEventType.getSimpleName());

        gameRule.conditionProperty().set(theMatchEventCondition);

        updateFilterConditions();
        updateActions();
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
            if (theAction instanceof SetGameObjectInstancePropertyAction) {
                SetGameObjectInstancePropertyEditorController theController = setGameObjectInstancePropertyEditorFactory.createFor(eventSheet.getGameScene(), (SetGameObjectInstancePropertyAction) theAction);
                actions.getChildren().add(theController.getView());
            }
            if (theAction instanceof SetGameObjectPropertyAction) {
                SetGameObjectPropertyEditorController theController = setGameObjectPropertyEditorControllerFactory.createFor(eventSheet.getGameScene(), (SetGameObjectPropertyAction) theAction);
                actions.getChildren().add(theController.getView());
            }
        }
    }

    private void updateFilterConditions() {
        filterCoditions.getChildren().clear();
        final MatchEventCondition theCondition = (MatchEventCondition) gameRule.conditionProperty().get();
        if (theCondition != null) {

            Class theFilterClass = knownEventTypes.get(theCondition.eventTypeProperty().get());

            GridPane theFilterConditions = new GridPane();
            theFilterConditions.setPadding(new Insets(5, 5, 5, 5));
            theFilterConditions.setVgap(5);
            filterCoditions.getChildren().add(theFilterConditions);

            int theRow = 0;

            for (Field theField : theFilterClass.getDeclaredFields()) {
                final String theFieldName = theField.getName();
                if (theField.getType().isAssignableFrom(Property.class)) {

                    Object theFilterValue = theCondition.getFilterValue(theFieldName);

                    Label theLabel = new Label(theFieldName + ":");
                    theFilterConditions.add(theLabel, 0, theRow);

                    ParameterizedType theParamType = (ParameterizedType) theField.getGenericType();
                    Type thePropertyType = theParamType.getActualTypeArguments()[0];
                    if (thePropertyType == String.class) {
                        TextField theTextfield = controlFactory.createTextField(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observableValue, String aOldValue,
                                                String aNewValue) {
                                theCondition.setFilterValue(theFieldName, aNewValue);
                            }
                        });
                        if (theFilterValue != null) {
                            theTextfield.setText((String) theCondition.getFilterValue(theFieldName));
                        }

                        theFilterConditions.add(theTextfield, 1, theRow);
                    }
                    if (thePropertyType == GameKeyCode.class) {
                        final ComboBox theCombobox = controlFactory.createKeyCodeCombobox(theFilterValue);
                        theCombobox.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                theCondition.setFilterValue(theFieldName, theCombobox.getValue());
                            }
                        });
                        theFilterConditions.add(theCombobox, 1, theRow);
                    }
                    if (thePropertyType == GameObject.class) {
                        final ComboBox theCombobox = controlFactory.createGameObjectCombobox(theFilterValue, eventSheet.getGameScene());
                        theCombobox.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                theCondition.setFilterValue(theFieldName, theCombobox.getValue());
                            }
                        });
                        theFilterConditions.add(theCombobox, 1, theRow);
                    }
                    if (thePropertyType == GameObjectInstance.class) {
                        final ComboBox theCombobox = controlFactory.createGameObjectInstanceCombobox(theFilterValue, eventSheet.getGameScene());
                        theCombobox.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                theCondition.setFilterValue(theFieldName, theCombobox.getValue());
                            }
                        });
                        theFilterConditions.add(theCombobox, 1, theRow);
                    }

                    theRow++;
                }
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
