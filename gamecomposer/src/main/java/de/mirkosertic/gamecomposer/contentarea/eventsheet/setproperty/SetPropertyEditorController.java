package de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.SetPropertyAction;
import de.mirkosertic.gameengine.type.TextExpression;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

        propertyValueTextField.setVisible(true);
    }

    private void onTextfieldChange() {
        action.propertyValueProperty().set(new TextExpression(propertyValueTextField.getText()));
    }

    public Node getView() {
        return view;
    }
}
