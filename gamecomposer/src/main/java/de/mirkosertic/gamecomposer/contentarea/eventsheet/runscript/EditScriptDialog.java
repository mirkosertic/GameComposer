package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscript;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.process.GameProcess;
import de.mirkosertic.gameengine.script.RunScriptAction;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.type.Script;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.inject.Inject;

public class EditScriptDialog {

    @Inject
    PersistenceManager persistenceManager;

    @FXML
    WebView editorView;

    @FXML
    TextArea compileErrors;

    private RunScriptAction action;
    private Stage modalStage;
    private GameScene gameScene;

    public void initialize(GameScene aGameScene, RunScriptAction aAction, Stage aModalStage) {
        action = aAction;
        modalStage = aModalStage;
        gameScene = aGameScene;

        editorView.setContextMenuEnabled(false);
        editorView.getEngine().setJavaScriptEnabled(true);
        editorView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue,
                    Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    initializeHTML();
                }
            }
        });
        editorView.getEngine().load(EditScriptDialog.class.getResource("/ace/editor.html").toExternalForm());
    }

    private void initializeHTML() {
        Document theDocument = editorView.getEngine().getDocument();
        Element theEditorElement = theDocument.getElementById("editor");

        theEditorElement.setTextContent(action.scriptProperty().get().script);

        editorView.getEngine().executeScript("initeditor()");
    }

    private boolean test(Script aScript) {
        LUAScriptEngine theEngine = null;
        try {

            // We only want to test on a clone
            // so the test does not change enything
            GameScene theClone = persistenceManager.cloneSceneForPreview(gameScene);

            // Execute a single run for verification
            GameObject theObject = new GameObject(theClone, "dummy");
            GameObjectInstance theInstance = theClone.createFrom(theObject);
            theEngine = theClone.getRuntime().getScriptEngineFactory().createNewEngine(aScript);
            theEngine.registerObject("instance", theInstance);
            theEngine.registerObject("scene", theClone);

            Object theResult = theEngine.proceedGame(100, 16);
            if (theResult == null) {
                throw new RuntimeException("Got NULL as a response, expected " + GameProcess.ProceedResult.STOPPED+" or " + GameProcess.ProceedResult.CONTINUE_RUNNING);
            }

            GameProcess.ProceedResult theResultAsEnum = GameProcess.ProceedResult.valueOf(theResult.toString());

            theEngine.shutdown();

            compileErrors.setText("Got response : " + theResultAsEnum);

            return true;
        } catch (Exception e) {

            StringWriter theWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(theWriter));

            compileErrors.setText("Exception : " + theWriter);
        } finally {
            if (theEngine != null) {
                theEngine.shutdown();
            }
        }
        return false;
    }

    @FXML
    public void onOk() {
        String theContent = (String) editorView.getEngine().executeScript("getvalue()");
        Script theNewScript = new Script(theContent);

        //if (test(theNewScript)) {
            action.scriptProperty().set(theNewScript);

            modalStage.close();
        //}
    }

    @FXML
    public void onTest() {
        String theContent = (String) editorView.getEngine().executeScript("getvalue()");
        Script theNewScript = new Script(theContent);
        test(theNewScript);
    }

    @FXML
    public void onCancel() {
        modalStage.close();
    }

    public void performEditing() {
        modalStage.showAndWait();
    }
}