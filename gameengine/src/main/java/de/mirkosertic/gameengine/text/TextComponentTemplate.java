package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.TextExpression;

import java.util.HashMap;
import java.util.Map;

public class TextComponentTemplate extends GameComponentTemplate<TextComponent> implements Text {

    private final Property<Font> font;
    private final Property<TextExpression> textExpression;
    private final Property<Color> color;

    private final GameObject owner;

    @UsedByReflection
    public TextComponentTemplate(GameEventManager aEventManager, GameObject aOwner) {
        font = registerProperty(new Property<Font>(Font.class, this, FONT_PROPERTY, Font.DEFAULT_FONT, aEventManager));
        textExpression = registerProperty(new Property<TextExpression>(TextExpression.class, this, TEXT_EXPRESSION_PROPERTY, new TextExpression(""), aEventManager));
        color = registerProperty(new Property<Color>(Color.class, this, COLOR_PROPERTY, Color.WHITE, aEventManager));
        owner = aOwner;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public Property<Font> fontProperty() {
        return font;
    }

    public Property<TextExpression> textExpressionProperty() {
        return textExpression;
    }

    public Property<Color> colorProperty() {
        return color;
    }

    public TextComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new TextComponent(aInstance, this);
    }

    @Override
    public String getTypeKey() {
        return TextComponent.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TextComponent.TYPE_ATTRIBUTE, TextComponent.TYPE);
        theResult.put(FONT_PROPERTY, font.get().serialize());
        theResult.put(TEXT_EXPRESSION_PROPERTY, textExpression.get().serialize());
        theResult.put(COLOR_PROPERTY, color.get().serialize());
        return theResult;
    }

    public static TextComponentTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        TextComponentTemplate theTemplate = new TextComponentTemplate(aEventManager, aOwner);
        theTemplate.font.setQuietly(Font.deserialize((Map<String, Object>) aSerializedData.get(FONT_PROPERTY)));
        theTemplate.textExpression.setQuietly(TextExpression.deserialize((Map<String, Object>) aSerializedData.get(TEXT_EXPRESSION_PROPERTY)));
        theTemplate.color.setQuietly(Color.deserialize((Map<String, Object>) aSerializedData.get(COLOR_PROPERTY)));
        return theTemplate;
    }
}
