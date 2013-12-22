package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Color;
import de.mirkosertic.gameengine.types.Font;
import de.mirkosertic.gameengine.types.TextExpression;

import java.util.HashMap;
import java.util.Map;

public class TextComponentTemplate extends GameComponentTemplate<TextComponent> {

    private final Property<Font> font;
    private final Property<TextExpression> textExpression;
    private final Property<Color> color;

    private final GameObject owner;

    public TextComponentTemplate(GameEventManager aEventManager, GameObject aOwner) {
        font = registerProperty(new Property<Font>(this, "font", Font.DEFAULT_FONT, aEventManager));
        textExpression = registerProperty(new Property<TextExpression>(this, "textExpression", new TextExpression(""), aEventManager));
        color = registerProperty(new Property<Color>(this, "color", Color.WHITE, aEventManager));
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
        theResult.put("font", font.get().serialize());
        theResult.put("textExpression", textExpression.get().serialize());
        theResult.put("color", color.get().serialize());
        return theResult;
    }

    public static TextComponentTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        TextComponentTemplate theTemplate = new TextComponentTemplate(aEventManager, aOwner);
        theTemplate.font.setQuietly(Font.deserialize((Map<String, Object>) aSerializedData.get("font")));
        theTemplate.textExpression.setQuietly(TextExpression.deserialize((Map<String, Object>) aSerializedData.get("textExpression")));
        theTemplate.color.setQuietly(Color.deserialize((Map<String, Object>) aSerializedData.get("color")));
        return theTemplate;
    }
}
