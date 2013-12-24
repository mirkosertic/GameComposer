package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Color;
import de.mirkosertic.gameengine.types.Font;
import de.mirkosertic.gameengine.types.TextExpression;

public interface Text {

    Property<Font> fontProperty();

    Property<TextExpression> textExpressionProperty();

    Property<Color> colorProperty();
}
