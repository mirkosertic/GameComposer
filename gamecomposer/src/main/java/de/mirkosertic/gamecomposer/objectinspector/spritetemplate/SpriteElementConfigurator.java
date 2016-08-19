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
package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PersistentPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.AnimationPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.PositiveIntegerPropertyEditor;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.type.Animation;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = Sprite.class)
public class SpriteElementConfigurator implements ObjectInspectorElementConfigurator<Sprite> {

    private static final String CATEGORY_NAME = "60 Sprite";

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    MessageBox messageBox;

    @Inject
    AnimationEditorDialogFactory animationEditorDialogFactory;

    @Override
    public List<PropertySheet.Item> getItemsFor(Sprite aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.speedProperty(), "Animation speed", "The animation speed in frames / second",
                Optional.of(PositiveIntegerPropertyEditor.class)));
        theResult.add(new PersistentPropertyEditorItem<>(persistenceManager, CATEGORY_NAME, aObject.currentAnimationProperty(), "Current animation", "The current animation",
              Optional.of(AnimationPropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");

        if (aObject instanceof SpriteBehaviorTemplate) {
            final SpriteBehaviorTemplate theTemplate = (SpriteBehaviorTemplate) aObject;
            for (Animation theAnimation : theTemplate.getAnimations()) {
                theResult.add(new AnimationPropertySheetItem(persistenceManager, theAnimation, theTemplate, CATEGORY_NAME, theAnimation.getName(), "An animation", animationEditorDialogFactory));
            }
            theActions.addAction(new ActionPropertyEditorItem.Action("New animation...", aEvent -> {
                AnimationEditorDialog theDialog = animationEditorDialogFactory.createFor((Node)aEvent.getSource(), theTemplate, new Animation(""));
                Animation theAnimation = theDialog.performEditing();
                if (theAnimation != null) {
                    theTemplate.addAnimation(theAnimation);
                }
            }));
        }

        theActions.addAction(new ActionPropertyEditorItem.Action("Delete behavior...", aEvent -> {
            if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete behavior", "Do you really want to delete this behavior?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                aObject.delete();
            }
        }));
        theResult.add(theActions);

        return theResult;
    }
}