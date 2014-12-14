package de.mirkosertic.gamecomposer.objectinspector.playerscore;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.playerscore.PlayerScore;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;

@ObjectInspectorElementConfiguratorType(clazz = PlayerScore.class)
public class PlayerScoreElementConfigurator implements ObjectInspectorElementConfigurator<PlayerScore> {

    @Override
    public List<PropertySheet.Item> getItemsFor(PlayerScore aObject) {
        return new ArrayList<>();
    }
}
