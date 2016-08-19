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

import de.mirkosertic.gameengine.core.Game;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.prefs.Preferences;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameComposerController {

    private static final String GAME_DIRECTORY_PREF_KEY = "GameDirectory";
    private static final String GAME_EXPORTDIRECTORY_HTML_PREF_KEY = "GameExportDirectoryHTML";
    private static final String GAME_EXPORTDIRECTORY_ANDROID_PREF_KEY = "GameExportDirectoryAndroid";

    @Inject
    @ObjectInspector
    Controller objectInspector;

    @Inject
    @ContentArea
    Controller contentArea;

    @Inject
    @ProjectStructure
    Controller projectStructure;

    @Inject
    Event<Object> eventGateway;

    @FXML
    BorderPane borderPane;

    @FXML
    Menu exportMenu;

    @FXML
    Label statusBar;

    @Inject
    Event<StatusEvent> statusEvent;

    private Preferences directoryPreferences;
    private Stage stage;

    public void initialize(Stage aStage) {

        stage = aStage;

        directoryPreferences = Preferences.userNodeForPackage(GameComposerController.class);
        exportMenu.setDisable(true);

        SplitPane theSplitPane = new SplitPane();
        theSplitPane.setOrientation(Orientation.HORIZONTAL);
        theSplitPane.getItems().addAll(objectInspector.getView(), contentArea.getView(), projectStructure.getView());
        theSplitPane.setDividerPositions(0.1, 0.1);

        borderPane.setCenter(theSplitPane);

        eventGateway.fire(new ApplicationStartedEvent());
    }

    @FXML
    public void onClose() {
        try {
            directoryPreferences.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.exit();
    }

    @FXML
    public void onNew() {

        DirectoryChooser theDirectoryChooser = new DirectoryChooser();
        theDirectoryChooser.setTitle("Choose target directory");
        File theProjectDirectory = theDirectoryChooser.showDialog(stage);
        if (theProjectDirectory != null) {
            Game theNewGame = new Game();
            theNewGame.nameProperty().set("New game");
            eventGateway.fire(new NewGameEvent(theProjectDirectory, theNewGame));

            directoryPreferences.put(GAME_DIRECTORY_PREF_KEY, theProjectDirectory.toString());
            exportMenu.setDisable(false);

            statusEvent.fire(new StatusEvent("New game created", StatusEvent.Severity.INFO));
        }
    }

    @FXML
    public void onSave() {
        eventGateway.fire(new SaveGameEvent());
        exportMenu.setDisable(false);
    }

    @FXML
    public void onOpen() {
        FileChooser theFileChooser = new FileChooser();
        theFileChooser.setTitle("Open game");

        String theLastDirectory = directoryPreferences.get(GAME_DIRECTORY_PREF_KEY, null);
        if (theLastDirectory != null) {
            File theDirectory = new File(theLastDirectory);
            if (theDirectory.isFile()) {
                theDirectory = theDirectory.getParentFile();
            }
            if (theDirectory.exists()) {
                theFileChooser.setInitialDirectory(theDirectory);
            }
        }

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Game files (game.json)", "game.json");
        theFileChooser.getExtensionFilters().add(extFilter);

        File theSelectedFile = theFileChooser.showOpenDialog(null);
        if (theSelectedFile != null) {
            eventGateway.fire(new LoadGameEvent(theSelectedFile));

            directoryPreferences.put(GAME_DIRECTORY_PREF_KEY, theSelectedFile.getParentFile().toString());
            exportMenu.setDisable(false);

            statusEvent.fire(new StatusEvent("Game loaded", StatusEvent.Severity.INFO));
        }
    }

    @FXML
    public void onSaveAndExportHTML5GWT() {
        DirectoryChooser theDirectoryChooser = new DirectoryChooser();
        theDirectoryChooser.setTitle("Choose target directory");

        String theLastExportDir = directoryPreferences.get(GAME_EXPORTDIRECTORY_HTML_PREF_KEY, null);
        if (theLastExportDir != null) {
            File theFile = new File(theLastExportDir);
            if (theFile.exists() && theFile.isDirectory()) {
                theDirectoryChooser.setInitialDirectory(theFile);
            }
        }

        File theTargetDirectory = theDirectoryChooser.showDialog(stage);
        if (theTargetDirectory != null) {
            eventGateway.fire(new ExportGameHTML5GWTEvent(theTargetDirectory));

            directoryPreferences.put(GAME_EXPORTDIRECTORY_HTML_PREF_KEY, theTargetDirectory.toString());
        }
    }

    @FXML
    public void onSaveAndExportHTML5Dragome() {
        DirectoryChooser theDirectoryChooser = new DirectoryChooser();
        theDirectoryChooser.setTitle("Choose target directory");

        String theLastExportDir = directoryPreferences.get(GAME_EXPORTDIRECTORY_HTML_PREF_KEY, null);
        if (theLastExportDir != null) {
            File theFile = new File(theLastExportDir);
            if (theFile.exists() && theFile.isDirectory()) {
                theDirectoryChooser.setInitialDirectory(theFile);
            }
        }

        File theTargetDirectory = theDirectoryChooser.showDialog(stage);
        if (theTargetDirectory != null) {
            eventGateway.fire(new ExportGameHTML5DragomeEvent(theTargetDirectory));

            directoryPreferences.put(GAME_EXPORTDIRECTORY_HTML_PREF_KEY, theTargetDirectory.toString());
        }
    }

    @FXML
    public void onSaveAndExportHTML5TeaVM() {
        DirectoryChooser theDirectoryChooser = new DirectoryChooser();
        theDirectoryChooser.setTitle("Choose target directory");

        String theLastExportDir = directoryPreferences.get(GAME_EXPORTDIRECTORY_HTML_PREF_KEY, null);
        if (theLastExportDir != null) {
            File theFile = new File(theLastExportDir);
            if (theFile.exists() && theFile.isDirectory()) {
                theDirectoryChooser.setInitialDirectory(theFile);
            }
        }

        File theTargetDirectory = theDirectoryChooser.showDialog(stage);
        if (theTargetDirectory != null) {
            eventGateway.fire(new ExportGameHTML5TeaVMEvent(theTargetDirectory));

            directoryPreferences.put(GAME_EXPORTDIRECTORY_HTML_PREF_KEY, theTargetDirectory.toString());
        }
    }

    @FXML
    public void onSaveAndExportAndroid() {
        DirectoryChooser theDirectoryChooser = new DirectoryChooser();
        theDirectoryChooser.setTitle("Choose target directory");

        String theLastExportDir = directoryPreferences.get(GAME_EXPORTDIRECTORY_ANDROID_PREF_KEY, null);
        if (theLastExportDir != null) {
            File theFile = new File(theLastExportDir);
            if (theFile.exists() && theFile.isDirectory()) {
                theDirectoryChooser.setInitialDirectory(theFile);
            }
        }

        File theTargetDirectory = theDirectoryChooser.showDialog(stage);
        if (theTargetDirectory != null) {
            eventGateway.fire(new ExportGameAndroidEvent(theTargetDirectory));

            directoryPreferences.put(GAME_EXPORTDIRECTORY_ANDROID_PREF_KEY, theTargetDirectory.toString());
        }
    }

    public void handleStatus(final @Observes StatusEvent aStatusEvent) {
        Runnable theRunnable = () -> {
            switch (aStatusEvent.getSeverity()) {
                case ERROR:
                    statusBar.setTextFill(Color.RED);
                    break;
                case INFO:
                    statusBar.setTextFill(Color.BLACK);
                    break;
            }
            statusBar.setText(aStatusEvent.getMessage());
        };
        if (Platform.isFxApplicationThread()) {
            theRunnable.run();
        } else {
            Platform.runLater(theRunnable);
        }
    }
}