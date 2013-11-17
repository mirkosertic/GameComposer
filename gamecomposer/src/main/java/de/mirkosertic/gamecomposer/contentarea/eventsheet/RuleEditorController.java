package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mirkosertic.gameengine.action.SetPropertyCommand;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.action.SetGameObjectInstancePropertyAction;
import de.mirkosertic.gameengine.action.SetGameObjectPropertyAction;

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
        knownEventTypes.put(KeyPressedGame.class.getSimpleName(), KeyPressedGame.class);
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

            GridPane theActionProperties = new GridPane();
            theActionProperties.setPadding(new Insets(5, 5, 5, 5));
            theActionProperties.setVgap(5);
            int theActionRow = 0;

            for (Field theField : theAction.getClass().getDeclaredFields()) {
                theField.setAccessible(true);
                final String theFieldName = theField.getName();
                if (theField.getType().isAssignableFrom(Property.class)) {

                    ParameterizedType theParamType = (ParameterizedType) theField.getGenericType();
                    Type thePropertyType = theParamType.getActualTypeArguments()[0];
                    if (thePropertyType == ResourceName.class) {
                        try {
                            Label theLabel = new Label(theFieldName + ":");
                            theLabel.setAlignment(Pos.CENTER_LEFT);
                            theActionProperties.add(theLabel, 0, theActionRow);

                            Property<ResourceName> theProperty = (Property<ResourceName>) theField.get(theAction);
                            Node theNode = controlFactory.createResourceSelectorFor(theProperty, eventSheet.getGameScene(), view);
                            theActionProperties.add(theNode, 1, theActionRow);
                        } catch (IllegalAccessException e) {
                            // should not happen
                            throw new RuntimeException(e);
                        }
                    }
                    if (thePropertyType == String.class) {
                        try {
                            Label theLabel = new Label(theFieldName + ":");
                            theLabel.setAlignment(Pos.CENTER_LEFT);
                            theActionProperties.add(theLabel, 0, theActionRow);

                            final Property<String> theProperty = (Property<String>) theField.get(theAction);
                            TextField theNode = new TextField();
                            PropertyBinder.bindUIToBean(theProperty, theNode.textProperty());
                            theActionProperties.add(theNode, 1, theActionRow);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (thePropertyType == Boolean.class) {
                        try {
                            final Property<Boolean> theProperty = (Property<Boolean>) theField.get(theAction);
                            CheckBox theNode = new CheckBox();
                            theNode.setText(theFieldName);
                            PropertyBinder.bindUIToBean(theProperty, theNode.selectedProperty());
                            theActionProperties.add(theNode, 1, theActionRow);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (thePropertyType == GameKeyCode.class) {
                        try {
                            Label theLabel = new Label(theFieldName + ":");
                            theLabel.setAlignment(Pos.CENTER_LEFT);
                            theActionProperties.add(theLabel, 0, theActionRow);


                            final Property<GameKeyCode> theProperty = (Property<GameKeyCode>) theField.get(theAction);
                            ComboBox theNode = controlFactory.createKeyCodeCombobox(theProperty.get());
                            PropertyBinder.bindUIToBean(theProperty, theNode.valueProperty());
                            theActionProperties.add(theNode, 1, theActionRow);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (thePropertyType == GameObject.class) {
                        try {
                            Label theLabel = new Label(theFieldName + ":");
                            theLabel.setAlignment(Pos.CENTER_LEFT);
                            theActionProperties.add(theLabel, 0, theActionRow);


                            final Property<GameObject> theProperty = (Property<GameObject>) theField.get(theAction);
                            ComboBox theNode = controlFactory.createGameObjectCombobox(theProperty.get(), eventSheet.getGameScene());
                            PropertyBinder.bindUIToBean(theProperty, theNode.valueProperty());
                            theActionProperties.add(theNode, 1, theActionRow);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (thePropertyType == GameObjectInstance.class) {
                        try {
                            Label theLabel = new Label(theFieldName + ":");
                            theLabel.setAlignment(Pos.CENTER_LEFT);
                            theActionProperties.add(theLabel, 0, theActionRow);

                            final Property<GameObjectInstance> theProperty = (Property<GameObjectInstance>) theField.get(theAction);
                            ComboBox theNode = controlFactory.createGameObjectInstanceCombobox(theProperty.get(), eventSheet.getGameScene());
                            PropertyBinder.bindUIToBean(theProperty, theNode.valueProperty());
                            theActionProperties.add(theNode, 1, theActionRow);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (thePropertyType == SetPropertyCommand.class) {
                        try {
                            final Property<SetPropertyCommand> theProperty = (Property<SetPropertyCommand>) theField.get(theAction);
                            //TODO: Implement here the generic stuff like property lookup
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    theActionRow++;
                }
            }

            actions.getChildren().add(theActionProperties);
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
