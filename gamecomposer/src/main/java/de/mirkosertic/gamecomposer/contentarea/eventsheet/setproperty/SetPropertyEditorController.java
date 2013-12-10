package de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gameengine.core.SetPropertyAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class SetPropertyEditorController implements Controller {

    class SupportedProperty {
        final String name;
        final Class type;

        SupportedProperty(String aName, Class aType) {
            name = aName;
            type = aType;
        }
    }

    @FXML
    ComboBox propertyName;

    @FXML
    TextField propertyValueTextField;

    @FXML
    CheckBox propertyValueCheckbox;

    @FXML
    ComboBox propertyValueComboBox;

    private GameScene gameScene;
    private SetPropertyAction action;
    private Node view;

    SetPropertyEditorController initialize(Node aView, GameScene aGameScene, SetPropertyAction aAction) {
        gameScene = aGameScene;
        view = aView;
        action = aAction;

        List<SupportedProperty> theSupportedProperties = new ArrayList<>();
        theSupportedProperties.add(new SupportedProperty("visible", Boolean.class));

        propertyName.getItems().clear();
        propertyName.setConverter(new StringConverter<SupportedProperty>() {
            @Override
            public String toString(SupportedProperty aValue) {
                return aValue.name;
            }

            @Override
            public SupportedProperty fromString(String aValue) {
                // Nonsense here
                return null;
            }
        });

        propertyValueTextField.setVisible(false);
        propertyValueTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                onTextfieldChange();
            }
        });
        propertyValueComboBox.setVisible(false);
        propertyValueComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object o2) {
                onComboboxChange();
            }
        });
        propertyValueCheckbox.setVisible(false);
        propertyValueCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                onCheckboxChange();
            }
        });

        propertyName.getItems().addAll(theSupportedProperties);
        if (action.propertyNameProperty().isNull()) {
            propertyName.getSelectionModel().select(0);
        } else {
            for (int i = 0; i < theSupportedProperties.size(); i++) {
                SupportedProperty theProp = theSupportedProperties.get(i);
                if (theProp.name.equals(action.propertyNameProperty().get())) {
                    propertyName.getSelectionModel().select(i);
                }
            }
        }

        return this;
    }

    @FXML
    public void onSelectProperty() {
        SupportedProperty theSelectedProperty = (SupportedProperty) propertyName.getSelectionModel().getSelectedItem();
        action.propertyNameProperty().set(theSelectedProperty.name);

        if (theSelectedProperty.type == String.class) {
            propertyValueTextField.setVisible(true);
            propertyValueComboBox.setVisible(false);
            propertyValueCheckbox.setVisible(false);

            Object theValue = action.propertyValueProperty().get();
            if (theValue instanceof String) {
                propertyValueTextField.setText((String) theValue);
            } else {
                action.propertyValueProperty().clear();
            }
        }
        if (theSelectedProperty.type == Boolean.class) {
            propertyValueTextField.setVisible(false);
            propertyValueComboBox.setVisible(false);
            propertyValueCheckbox.setVisible(true);

            Object theValue = action.propertyValueProperty().get();
            if (theValue instanceof Boolean) {
                propertyValueCheckbox.setSelected((Boolean) theValue);
            } else {
                action.propertyValueProperty().clear();
            }
        }
    }

    private void onTextfieldChange() {
        action.propertyValueProperty().set(propertyValueTextField.getText());
    }

    private void onComboboxChange() {
        action.propertyValueProperty().set(propertyValueComboBox.getValue());
    }

    private void onCheckboxChange() {
        action.propertyValueProperty().set(propertyValueCheckbox.isSelected());
    }

    public Node getView() {
        return view;
    }
}
