package de.mirkosertic.gamecomposer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.File;
import java.util.prefs.Preferences;

public class GameComposerController {

    private static final String GAME_DIRECTORY_PREF_KEY = "GameDirectory";

    @Inject
    @ObjectInspector
    ChildController objectInspector;

    @Inject
    @ContentArea
    ChildController contentArea;

    @Inject
    @ProjectStructure
    ChildController projectStructure;

    @Inject
    Event<ApplicationStartedEvent> applicationStartedEvent;

    @Inject
    Event<NewGameEvent> newGameEvent;

    @Inject
    Event<LoadGameEvent> loadGameEvent;

    @Inject
    Event<SaveGameEvent> saveGameEvent;

    @FXML
    HBox childViews;

    private Preferences directoryPreferences;

    public void initialize(Stage aStage) {

        directoryPreferences = Preferences.userNodeForPackage(GameComposerController.class);

        childViews.getChildren().add(objectInspector.getView());
        childViews.getChildren().add(contentArea.getView());
        childViews.getChildren().add(projectStructure.getView());

        HBox.setHgrow(objectInspector.getView(), Priority.SOMETIMES);
        HBox.setHgrow(contentArea.getView(), Priority.ALWAYS);
        HBox.setHgrow(projectStructure.getView(), Priority.SOMETIMES);

        applicationStartedEvent.fire(new ApplicationStartedEvent());
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
        File theProjectDirectory = theDirectoryChooser.showDialog(null);
        if (theProjectDirectory != null) {
            newGameEvent.fire(new NewGameEvent(theProjectDirectory));

            directoryPreferences.put(GAME_DIRECTORY_PREF_KEY, theProjectDirectory.toString());
        }
    }

    @FXML
    public void onSave() {
        saveGameEvent.fire(new SaveGameEvent());
    }

    @FXML
    public void onOpen() {
        FileChooser theFileChooser = new FileChooser();

        String theLastDirectory = directoryPreferences.get(GAME_DIRECTORY_PREF_KEY, null);
        if (theLastDirectory != null) {
            File theDirectory = new File(theLastDirectory);
            if (theDirectory.isFile()) {
                theDirectory = theDirectory.getParentFile();
            }
            theFileChooser.setInitialDirectory(theDirectory);
        }

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Game files (game.json)","game.json");
        theFileChooser.getExtensionFilters().add(extFilter);

        File theSelectedFile = theFileChooser.showOpenDialog(null);
        if (theSelectedFile != null) {
            loadGameEvent.fire(new LoadGameEvent(theSelectedFile));

            directoryPreferences.put(GAME_DIRECTORY_PREF_KEY, theSelectedFile.getParentFile().toString());
        }
    }
}
