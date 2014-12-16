package de.mirkosertic.gamecomposer.objectinspector.playerscore;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.ScoreValuePropertyEditor;
import de.mirkosertic.gameengine.playerscore.PlayerScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = PlayerScore.class)
public class PlayerScoreElementConfigurator implements ObjectInspectorElementConfigurator<PlayerScore> {

    private static final String CATEGORY_NAME = "Player score";

    @Override
    public List<PropertySheet.Item> getItemsFor(PlayerScore aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.scoreValueProperty(), "Score value", "The current score value", Optional
                .of(ScoreValuePropertyEditor.class)));
        return theResult;
    }
}
