package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.type.Animation;

import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.Optional;

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