package de.mirkosertic.gamecomposer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

public class MessageBox {

    public enum ButtonType {
        YES, NO
    }

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public ButtonType showMessageBox(Node aParentNode, String aTitle, String aMessage, ButtonType... aButtons) {
        try (InputStream fxml = MessageBoxController.class.getResourceAsStream("MessageBox.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.MessageBox");
            theLoader.setResources(theBundle);
            Parent root = theLoader.load(fxml);
            MessageBoxController theController = theLoader.getController();

            Stage theModalStage = new Stage();
            theModalStage.setTitle(aTitle);
            theModalStage.initModality(Modality.APPLICATION_MODAL);
            theModalStage.initOwner(aParentNode.getScene().getWindow());
            theModalStage.setScene(new Scene(root));

            theController.initialize(theModalStage, aMessage, aButtons);

            theModalStage.showAndWait();

            return theController.getModalResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
