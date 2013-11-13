package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gameengine.action.PlaySoundAction;
import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameKeyCode;
import de.mirkosertic.gameengine.core.GameLoopRunEvent;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.KeyPressedGameEvent;
import de.mirkosertic.gameengine.core.KeyReleasedGameEvent;
import de.mirkosertic.gameengine.core.MatchEventCondition;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.physics.GameObjectCollisionEvent;
import de.mirkosertic.gameengine.types.ResourceName;

public class RuleEditorController implements ChildController {

    @FXML
    TextField ruleName;

    @FXML
    ComboBox eventType;

    @FXML
    VBox filterCoditions;

    @FXML
    VBox actions;

    private Node view;
    private GameRule gameRule;
    private EventSheet eventSheet;
    private EventSheetEditorController parentController;

    private Map<String, Class> knownEventTypes;

    RuleEditorController initialize(EventSheetEditorController aParentController, BorderPane aView,
            EventSheet aEventSheet, GameRule aGameRule) {
        eventSheet = aEventSheet;
        view = aView;
        parentController = aParentController;
        gameRule = aGameRule;

        knownEventTypes = new HashMap<>();
        knownEventTypes.put(GameLoopRunEvent.class.getSimpleName(), GameLoopRunEvent.class);
        knownEventTypes.put(KeyPressedGameEvent.class.getSimpleName(), KeyPressedGameEvent.class);
        knownEventTypes.put(KeyReleasedGameEvent.class.getSimpleName(), KeyReleasedGameEvent.class);
        knownEventTypes.put(GameObjectCollisionEvent.class.getSimpleName(), GameObjectCollisionEvent.class);

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
            HBox theActionBox = new HBox();

            VBox theActionInfo = new VBox();
            HBox.setMargin(theActionBox, new Insets(0, 0, 5, 0));

            Hyperlink theRemoveActionLink = new Hyperlink("Remove");
            theActionInfo.getChildren().add(new Label(theAction.getClass().getSimpleName()));
            theActionInfo.getChildren().add(theRemoveActionLink);

            HBox.setHgrow(theActionInfo, Priority.ALWAYS);
            theActionBox.getChildren().add(theActionInfo);

            VBox theProperties = new VBox();
            HBox.setHgrow(theProperties, Priority.ALWAYS);

            for (Field theField : theAction.getClass().getDeclaredFields()) {
                theField.setAccessible(true);
                final String theFieldName = theField.getName();
                if (theField.getType().isAssignableFrom(Property.class)) {
                    HBox thePropertyHBox = new HBox();
                    theProperties.setMaxWidth(Double.MAX_VALUE);

                    Label theLabel = new Label(theFieldName + ":");
                    theLabel.setAlignment(Pos.CENTER_LEFT);
                    HBox.setMargin(theLabel, new Insets(0, 0, 5, 0));

                    thePropertyHBox.getChildren().add(theLabel);

                    ParameterizedType theParamType = (ParameterizedType) theField.getGenericType();
                    if (theParamType.getActualTypeArguments()[0] == ResourceName.class) {

                        try {
                            ResourceName theResourceNameProperty = (ResourceName) ((Property) theField.get(theAction))
                                    .get();

                            VBox theResourceInfo = new VBox();
                            theResourceInfo.setStyle("-fx-background-color: red");
                            HBox.setMargin(theResourceInfo, new Insets(0, 5, 0, 0));

                            TextField theResourceName = new TextField();
                            theResourceName.setDisable(true);
                            theResourceName.setEditable(false);
                            theResourceName.setMaxWidth(Double.MAX_VALUE);
                            if (theResourceNameProperty != null) {
                                theResourceName.setText(theResourceNameProperty.name);
                            }
                            HBox.setHgrow(theResourceName, Priority.SOMETIMES);
                            theResourceInfo.getChildren().add(theResourceName);

                            Hyperlink theSelectAssetLink = new Hyperlink("Select asset...");
                            theResourceInfo.getChildren().add(theSelectAssetLink);

                            HBox.setMargin(theSelectAssetLink, new Insets(5, 0, 0, 0));

                            thePropertyHBox.getChildren().add(theResourceInfo);
                        } catch (IllegalAccessException e) {
                            // should not happen
                            throw new RuntimeException(e);
                        }
                    }
                    theProperties.getChildren().add(thePropertyHBox);
                }
            }

            theActionBox.getChildren().add(theProperties);

            actions.getChildren().add(theActionBox);
        }
    }

    private void updateFilterConditions() {
        filterCoditions.getChildren().clear();
        final MatchEventCondition theCondition = (MatchEventCondition) gameRule.conditionProperty().get();
        if (theCondition != null) {

            Class theFilterClass = knownEventTypes.get(theCondition.eventTypeProperty().get());

            for (Field theField : theFilterClass.getDeclaredFields()) {
                final String theFieldName = theField.getName();
                if (theField.getType().isAssignableFrom(ReadOnlyProperty.class)) {

                    Object theFilterValue = theCondition.getFilterValue(theFieldName);

                    HBox thePropertyHBox = new HBox();
                    VBox.setMargin(thePropertyHBox, new Insets(0, 0, 5, 0));

                    thePropertyHBox.setMaxWidth(Double.MAX_VALUE);

                    Label theLabel = new Label(theFieldName + ":");
                    theLabel.setAlignment(Pos.CENTER_LEFT);
                    theLabel.setMaxHeight(Double.MAX_VALUE);

                    thePropertyHBox.getChildren().add(theLabel);

                    ParameterizedType theParamType = (ParameterizedType) theField.getGenericType();
                    if (theParamType.getActualTypeArguments()[0] == String.class) {
                        TextField theTextfield = new TextField();
                        if (theFilterValue != null) {
                            theTextfield.setText((String) theCondition.getFilterValue(theFieldName));
                        }
                        theTextfield.textProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observableValue, String aOldValue,
                                    String aNewValue) {
                                theCondition.setFilterValue(theFieldName, aNewValue);
                            }
                        });

                        thePropertyHBox.getChildren().add(theTextfield);
                    }
                    if (theParamType.getActualTypeArguments()[0] == GameKeyCode.class) {
                        final ComboBox theCombobox = new ComboBox();
                        theCombobox.getItems().clear();
                        theCombobox.getItems().addAll(GameKeyCode.values());
                        theCombobox.getSelectionModel().select(theFilterValue);
                        theCombobox.setConverter(new StringConverter<GameKeyCode>() {
                            @Override
                            public String toString(GameKeyCode o) {
                                return o.name();
                            }

                            @Override
                            public GameKeyCode fromString(String s) {
                                return null;
                            }
                        });
                        theCombobox.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                theCondition.setFilterValue(theFieldName, theCombobox.getValue());
                            }
                        });

                        thePropertyHBox.getChildren().add(theCombobox);
                    }
                    if ((theParamType.getActualTypeArguments()[0] == GameObjectInstance.class)
                            || ((theParamType.getActualTypeArguments()[0] == GameObject.class))) {
                        final ComboBox theCombobox = new ComboBox();
                        theCombobox.getItems().clear();
                        theCombobox.getItems().addAll(eventSheet.getGameScene().getObjects());
                        theCombobox.getItems().addAll(eventSheet.getGameScene().getInstances());
                        theCombobox.getSelectionModel().select(theFilterValue);
                        theCombobox.setConverter(new StringConverter<Object>() {
                            @Override
                            public String toString(Object o) {
                                if (o instanceof GameObject) {
                                    return ((GameObject) o).nameProperty().get();
                                }
                                return ((GameObjectInstance) o).nameProperty().get();
                            }

                            @Override
                            public Object fromString(String s) {
                                return null;
                            }
                        });
                        theCombobox.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                theCondition.setFilterValue(theFieldName, theCombobox.getValue());
                            }
                        });

                        thePropertyHBox.getChildren().add(theCombobox);
                    }

                    filterCoditions.getChildren().add(thePropertyHBox);
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
        gameRule.addAction(new PlaySoundAction());
        updateActions();
    }

    public void removed() {
        PropertyBinder.unbind(gameRule.nameProperty());
    }
}
