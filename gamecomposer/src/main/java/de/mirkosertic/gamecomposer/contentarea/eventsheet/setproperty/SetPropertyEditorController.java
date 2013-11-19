package de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty;

import java.lang.reflect.Type;

import de.mirkosertic.gamecomposer.Controller;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import de.mirkosertic.gameengine.action.SetPropertyAction;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;

public class SetPropertyEditorController implements Controller {

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

        propertyName.getItems().clear();
        propertyName.setDisable(true);
        propertyName.setConverter(new StringConverter<Property>() {
            @Override
            public String toString(Property aValue) {
                return aValue.getName();
            }

            @Override
            public Property fromString(String aValue) {
                // Nonsense here
                return null;
            }
        });

        propertyValueTextField.setVisible(false);
        propertyValueComboBox.setVisible(false);
        propertyValueCheckbox.setVisible(false);

        return this;
    }

    @FXML
    public void onSelectProperty() {
        Property theSelectedProperty = (Property) propertyName.getSelectionModel().getSelectedItem();
        action.propertyNameProperty().set(theSelectedProperty.getName());

        Type theType = theSelectedProperty.getClass().getTypeParameters()[0];
        if (theType == String.class) {
            propertyValueTextField.setVisible(true);
            propertyValueComboBox.setVisible(false);
            propertyValueCheckbox.setVisible(false);
        }
        if (theType == Float.class) {
            propertyValueTextField.setVisible(true);
            propertyValueComboBox.setVisible(false);
            propertyValueCheckbox.setVisible(false);
        }
        if (theType == Boolean.class) {
            propertyValueTextField.setVisible(false);
            propertyValueComboBox.setVisible(false);
            propertyValueCheckbox.setVisible(true);
        }
    }

    public Node getView() {
        return view;
    }
}
