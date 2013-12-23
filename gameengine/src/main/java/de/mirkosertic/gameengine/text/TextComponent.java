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

    public Property<Font> fontProperty() {
        return font;
    }

    public Property<Color> colorProperty() {
        return color;
    }

    public Property<TextExpression> textExpressionProperty() {
        return textExpression;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        theResult.put("font", font.get().serialize());
        theResult.put("textExpression", textExpression.get().serialize());
        theResult.put("color", color.get().serialize());
        return theResult;
    }

    @Override
    public TextComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(TextComponentTemplate.class);
    }

    public static TextComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        TextComponent theTemplate =  new TextComponent(aObjectInstance);
        theTemplate.font.setQuietly(Font.deserialize((Map<String, Object>) aSerializedData.get("font")));
        theTemplate.textExpression.setQuietly(TextExpression.deserialize((Map<String, Object>) aSerializedData.get("textExpression")));
        theTemplate.color.setQuietly(Color.deserialize((Map<String, Object>) aSerializedData.get("color")));
        return theTemplate;
    }
}