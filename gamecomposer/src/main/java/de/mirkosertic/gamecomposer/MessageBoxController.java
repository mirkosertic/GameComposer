package de.mirkosertic.gamecomposer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MessageBoxController {

    @FXML
    Label messageLabel;

    @FXML
    Button yesButton;

    @FXML
    Button noButton;

    private Stage stage;
    private MessageBox.ButtonType modalResult;

    public void initialize(Stage aStage, String aModalMessage, MessageBox.ButtonType[] aButtons) {
        stage = aStage;
        messageLabel.setText(aModalMessage);
        yesButton.setVisible(false);
        noButton.setVisible(false);
        for ( MessageBox.ButtonType theButton : aButtons) {
            switch (theButton) {
                case YES:
                    yesButton.setVisible(true);
                    break;
                case NO:
                    noButton.setVisible(true);
                    break;
            }
        }
    }

    @FXML
    public void onYesButton() {
        modalResult = MessageBox.ButtonType.YES;
        stage.close();
    }

    @FXML
    public void onNoButton() {
        modalResult = MessageBox.ButtonType.NO;
        stage.close();
    }

    public MessageBox.ButtonType getModalResult() {
        return modalResult;
    }
}