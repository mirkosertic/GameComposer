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
package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.GameSceneDeletedEvent;
import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PersistentPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.ColorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.GameObjectPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.arcaderacer.ArcadeRacerGameSceneEffect;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.starfield.StarfieldGameSceneEffect;
import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = GameScene.class)
public class GameSceneElementConfigurator implements ObjectInspectorElementConfigurator<GameScene> {

    private static final String CATEGORY_NAME = "02 Scene";

    interface EffectDescription {

        String description();

        GameSceneEffect create();
    }

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    MessageBox messageBox;

    @Inject
    Event<GameSceneDeletedEvent> event;

    @Override
    public List<PropertySheet.Item> getItemsFor(final GameScene aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the scene", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.cameraObjectProperty(), "Camera", "The default camera object", Optional.of(GameObjectPropertyEditor.class)));
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.defaultPlayerProperty(), "Default player", "The default player object", Optional.of(GameObjectPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.backgroundColorProperty(), "Background color", "The scene background color", Optional.of(ColorPropertyEditor.class)));

        Map<Class<? extends GameSceneEffect>, EffectDescription> theAvailableEffects = new HashMap<>();
        theAvailableEffects.put(StarfieldGameSceneEffect.class, new EffectDescription() {
            @Override
            public String description() {
                return "Starfield";
            }

            @Override
            public GameSceneEffect create() {
                return new StarfieldGameSceneEffect(aObject, aObject.getRuntime().getEventManager());
            }
        });

        theAvailableEffects.put(ArcadeRacerGameSceneEffect.class, new EffectDescription() {
            @Override
            public String description() {
                return "ArcadeRacer";
            }

            @Override
            public GameSceneEffect create() {
                return new ArcadeRacerGameSceneEffect(aObject, aObject.getRuntime().getEventManager());
            }
        });

        for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
            theAvailableEffects.remove(theEffect.getClass());
        }

        for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
            theAvailableEffects.remove(theEffect.getClass());
        }

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete scene...", aEvent -> {
            if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete scene", "Do you really want to delete the scene?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                persistenceManager.deleteScene(aObject);
                event.fire(new GameSceneDeletedEvent());
            }
        }));
        for (Map.Entry<Class<? extends GameSceneEffect>, EffectDescription> theEntry : theAvailableEffects.entrySet()) {
            theActions.addAction(new ActionPropertyEditorItem.Action("Add " + theEntry.getValue().description()+" effect",
                    aEvent -> aObject.addEffect(theEntry.getValue().create())));
        }
        theResult.add(theActions);

        //TODO: Implement Layout bound editing here
        return theResult;
    }
}