package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Color;
import de.mirkosertic.gameengine.types.Font;
import de.mirkosertic.gameengine.types.TextExpression;

import java.util.HashMap;
import java.util.Map;

public class TextComponent extends GameComponent {

    static final String TYPE = "TextComponent";

    private final GameObjectInstance objectInstance;

    private final Property<Font> font;
    private final Property<Color> color;
    private final Property<TextExpression> textExpression;

    TextComponent(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
        font = registerProperty(new Property<Font>(this, "font", Font.DEFAULT_FONT));
        color = registerProperty(new Property<Color>(this, "color", Color.WHITE));
        textExpression = registerProperty(new Property<TextExpression>(this, "textExpression", new TextExpression("")));
    }

    TextComponent(GameObjectInstance aObjectInstance, TextComponentTemplate aTemplate) {
        this(aObjectInstance);
        font.setQuietly(aTemplate.fontProperty().get());
        textExpression.setQuietly(aTemplate.textExpressionProperty().get());
        color.setQuietly(aTemplate.colorProperty().get());
    }

    public GameObjectInstance getObjectInstance() {
        return objectInstance;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public TextComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(TextComponentTemplate.class);
    }

    public static TextComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return new TextComponent(aObjectInstance);
    }
}