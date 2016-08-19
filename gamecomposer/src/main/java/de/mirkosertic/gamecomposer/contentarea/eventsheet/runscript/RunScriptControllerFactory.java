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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscript;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.script.RunScriptAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ActionEditorType(clazz = RunScriptAction.class)
public class RunScriptControllerFactory implements ActionControllerFactory<RunScriptController, RunScriptAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public RunScriptController createFor(GameScene aGameScene, GameRule aRule, RunScriptAction anAction) {
        try (InputStream fxml = RunScriptController.class.getResourceAsStream("RunScriptEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.runscript.RunScriptEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            RunScriptController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aRule, anAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}