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
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.script.RunScriptAction;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import javax.inject.Inject;

public class EditScriptDialogFactory {

    @Inject
    FXMLLoaderFactory loaderFactory;

    public EditScriptDialog createFor(GameScene aGameScene, Node aParent, RunScriptAction aAction) {
        try (InputStream fxml = EditScriptDialog.class.getResourceAsStream("EditScriptDialog.fxml")) {
            FXMLLoader theLoader = loaderFactory.createLoader();
            BorderPane thePane = theLoader.load(fxml);

            EditScriptDialog theDialog = theLoader.getController();

            Stage theModalStage = new Stage();
            theModalStage.setResizable(false);
            theModalStage.setTitle("Edit script");
            UndecoratorScene theUndecoratorScene = new UndecoratorScene(theModalStage, thePane);

            // Hacky, but works
            theUndecoratorScene.getUndecorator().setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

            theModalStage.initStyle(StageStyle.TRANSPARENT);
            theUndecoratorScene.setFill(Color.TRANSPARENT);

            theModalStage.setScene(theUndecoratorScene);
            //theModalStage.initModality(Modality.APPLICATION_MODAL);
            theModalStage.initOwner(aParent.getScene().getWindow());
            theDialog.initialize(aGameScene, aAction, theModalStage);

            return theDialog;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
