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

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.type.Animation;
import javafx.scene.Node;

import java.util.Optional;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class AnimationPropertySheetItem implements PropertySheet.Item {

    private final PersistenceManager persistenceManager;
    private final Animation animation;
    private final SpriteBehaviorTemplate template;
    private final String category;
    private final String name;
    private final String description;
    private final AnimationEditorDialogFactory animationEditorDialogFactory;

    public AnimationPropertySheetItem(PersistenceManager aPersistenceManager, Animation aAnimation, SpriteBehaviorTemplate aTermplate, String aCategory, String aName, String aDescription, AnimationEditorDialogFactory aAnimationEditorFactory) {
        persistenceManager = aPersistenceManager;
        animation = aAnimation;
        template = aTermplate;
        category = aCategory;
        name = aName;
        description = aDescription;
        animationEditorDialogFactory = aAnimationEditorFactory;
    }

    @Override
    public Class<?> getType() {
        return Animation.class;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Animation getValue() {
        return animation;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public SpriteBehaviorTemplate getTemplate() {
        return template;
    }

    @Override
    public void setValue(Object aValue) {
        template.replaceAnimation(animation, (Animation) aValue);
    }

    @Override
    public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
        return Optional.of(EditAnimationPropertyEditor.class);
    }

    public Animation editAnimationSequence(Node aSource) {
        AnimationEditorDialog theDialog = animationEditorDialogFactory.createFor(aSource, template, animation);
        Animation theAnimation = theDialog.performEditing();
        if (theAnimation != null) {
            template.replaceAnimation(animation, theAnimation);
        }
        return theAnimation;
    }
}