package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.objectinspector.EditorContext;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.Game;
import org.controlsfx.control.PropertySheet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@ObjectInspectorElementConfiguratorType(clazz = Game.class)
public class GameEditorElementConfigurator implements ObjectInspectorElementConfigurator<Game> {

    private static final String CATEGORY_NAME = "Game";

    @Inject
    EditorContext editorContext;

    @Override
    public List<PropertySheet.Item> getItemsFor(Game aObject) {
        ArrayList<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(editorContext, CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the game", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(editorContext, CATEGORY_NAME, aObject.enableWebGLProperty(), "WebGL enabled", "Is the WebGL renderer enabled or not", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(editorContext, CATEGORY_NAME, aObject.defaultSceneProperty(), "Default scene", "The default scene of the game", Optional.of(ScenePropertyEditor.class)));
        return theResult;
    }
}