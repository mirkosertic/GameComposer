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
package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameRule;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

public class RuleEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public RuleEditorController createFor(EventSheetEditorController aParentController, EventSheet aEventSheet, GameRule aGameRule) {
        try (InputStream fxml = RuleEditorController.class.getResourceAsStream("RuleEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.RuleEditor");
            theLoader.setResources(theBundle);
            BorderPane root = theLoader.load(fxml);

            RuleEditorController theController = theLoader.getController();

            return theController.initialize(aParentController, root, aEventSheet, aGameRule);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
