package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.GameAssetSelector;
import de.mirkosertic.gameengine.core.GameKeyCode;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.ResourceName;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.StringConverter;

import javax.inject.Inject;

public class ControlFactory {

    @Inject
    GameAssetSelector gameAssetSelector;

    public TextField createTextField(ChangeListener aChangeListener) {
        TextField theResult = new TextField();
        theResult.textProperty().addListener(aChangeListener);
        return theResult;
    }

    public ComboBox createKeyCodeCombobox(Object aSelectedValue) {
        ComboBox theCombobox = new ComboBox();
        theCombobox.getItems().clear();
        theCombobox.getItems().addAll(GameKeyCode.allKeysAsSortedList());
        theCombobox.getSelectionModel().select(aSelectedValue);
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
        return theCombobox;
    }

    public ComboBox createGameObjectCombobox(Object aSelectedValue, GameScene aGameScene) {
        ComboBox theCombobox = new ComboBox();
        theCombobox.getItems().clear();
        theCombobox.getItems().addAll(aGameScene.getObjects());
        theCombobox.getSelectionModel().select(aSelectedValue);
        theCombobox.setConverter(new StringConverter<Object>() {
            @Override
            public String toString(Object o) {
                return ((GameObject) o).nameProperty().get();
            }

            @Override
            public Object fromString(String s) {
                return null;
            }
        });
        return theCombobox;
    }

    public ComboBox createGameObjectInstanceCombobox(Object aSelectedValue, GameScene aGameScene) {
        ComboBox theCombobox = new ComboBox();
        theCombobox.getItems().clear();
        theCombobox.getItems().addAll(aGameScene.getInstances());
        theCombobox.getSelectionModel().select(aSelectedValue);
        theCombobox.setConverter(new StringConverter<Object>() {
            @Override
            public String toString(Object o) {
                return ((GameObjectInstance) o).nameProperty().get();
            }

            @Override
            public Object fromString(String s) {
                return null;
            }
        });
        return theCombobox;
    }

    public Node createResourceSelectorFor(final Property<ResourceName> aResourcProperty, final GameScene aGameScene, final Node aParentNode) {
        VBox theResourceSelector = new VBox();
        HBox.setMargin(theResourceSelector, new Insets(0, 5, 0, 0));

        final TextField theResourceNameTextField = new TextField();
        theResourceNameTextField.setDisable(true);
        theResourceNameTextField.setEditable(false);
        theResourceNameTextField.setMaxWidth(Double.MAX_VALUE);
        ResourceName theResourceName = aResourcProperty.get();
        if (theResourceName != null) {
            theResourceNameTextField.setText(theResourceName.name);
        }
        HBox.setHgrow(theResourceNameTextField, Priority.SOMETIMES);
        theResourceSelector.getChildren().add(theResourceNameTextField);

        Hyperlink theSelectAssetLink = new Hyperlink("Select asset...");
        theSelectAssetLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ResourceName theNewResourceName = gameAssetSelector.selectAudioAssetFrom(aGameScene, aParentNode.getScene().getWindow());
                if (theNewResourceName != null) {
                    theResourceNameTextField.setText(theNewResourceName.name);
                    aResourcProperty.set(theNewResourceName);
                }
            }
        });
        HBox.setMargin(theSelectAssetLink, new Insets(5, 0, 0, 0));
        theResourceSelector.getChildren().add(theSelectAssetLink);
        return theResourceSelector;
    }
}
