package de.mirkosertic.gamecomposer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
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
    private static final String GAME_EXPORTDIRECTORY_PREF_KEY = "GameExportDirectory";

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
    Event<Object> eventGateway;

    @FXML
    HBox childViews;

    @FXML
    Menu exportMenu;

    private Preferences directoryPreferences;

    public void initialize(Stage aStage) {

        directoryPreferences = Preferences.userNodeForPackage(GameComposerController.class);
        exportMenu.setDisable(true);

        childViews.getChildren().add(objectInspector.getView());
        childViews.getChildren().add(contentArea.getView());
        childViews.getChildren().add(projectStructure.getView());

        HBox.setHgrow(objectInspector.getView(), Priority.SOMETIMES);
        HBox.setHgrow(contentArea.getView(), Priority.ALWAYS);
        HBox.setHgrow(projectStructure.getView(), Priority.SOMETIMES);

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
        File theProjectDirectory = theDirectoryChooser.showDialog(null);
        if (theProjectDirectory != null) {
            eventGateway.fire(new NewGameEvent(theProjectDirectory));

            directoryPreferences.put(GAME_DIRECTORY_PREF_KEY, theProjectDirectory.toString());
            exportMenu.setDisable(false);
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
            theFileChooser.setInitialDirectory(theDirectory);
        }

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Game files (game.json)","game.json");
        theFileChooser.getExtensionFilters().add(extFilter);

        File theSelectedFile = theFileChooser.showOpenDialog(null);
        if (theSelectedFile != null) {
            eventGateway.fire(new LoadGameEvent(theSelectedFile));

            directoryPreferences.put(GAME_DIRECTORY_PREF_KEY, theSelectedFile.getParentFile().toString());
            exportMenu.setDisable(false);
        }
    }

    @FXML
    public void onSaveAndExportHTML5() {
        DirectoryChooser theDirectoryChooser = new DirectoryChooser();
        theDirectoryChooser.setTitle("Choose target directory");

        String theLastExportDir = directoryPreferences.get(GAME_EXPORTDIRECTORY_PREF_KEY, null);
        if (theLastExportDir != null) {
            theDirectoryChooser.setInitialDirectory(new File(theLastExportDir));
        }

        File theTargetDirectory = theDirectoryChooser.showDialog(null);
        if (theTargetDirectory != null) {
            eventGateway.fire(new ExportGameHTML5Event(theTargetDirectory));

            directoryPreferences.put(GAME_EXPORTDIRECTORY_PREF_KEY, theTargetDirectory.toString());
        }
    }
}