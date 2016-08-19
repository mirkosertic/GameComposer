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
package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.NewGameSceneEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PersistentPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.ScenePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.controlsfx.control.PropertySheet;

@Singleton
@ObjectInspectorElementConfiguratorType(clazz = Game.class)
public class GameEditorElementConfigurator implements ObjectInspectorElementConfigurator<Game> {

    private static final String CATEGORY_NAME = "01 Game";

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<NewGameSceneEvent> newGameSceneEvent;

    @Override
    public List<PropertySheet.Item> getItemsFor(Game aObject) {
        ArrayList<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the game", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.enableWebGLProperty(), "WebGL enabled", "Is the WebGL renderer enabled or not", Optional.empty()));
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.defaultSceneProperty(), "Default scene", "The default scene of the game", Optional.of(ScenePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.enableDebugProperty(), "Debug enabled", "Shall debug information such as frame rates be rendered?", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.enableNetworkingProperty(), "Networking enabled", "Shall the networking feature be enabled?", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.fireBaseURLProperty(), "Firebase URL", "The name of the firebase URL", Optional.of(StringPropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Create new scene", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newGameSceneEvent.fire(new NewGameSceneEvent());
            }
        }));
        theResult.add(theActions);

        return theResult;
    }
}