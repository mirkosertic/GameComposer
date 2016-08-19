/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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