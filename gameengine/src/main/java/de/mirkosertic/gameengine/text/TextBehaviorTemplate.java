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
package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.TextExpression;

import java.util.HashMap;
import java.util.Map;

public class TextBehaviorTemplate implements BehaviorTemplate<TextBehavior>, Text, Reflectable<TextClassInformation> {

    public static final BehaviorType TYPE = new BehaviorType("TextBehaviorTemplate");

    private static final TextClassInformation CIINSTANCE = new TextClassInformation();

    private final Property<Font> font;
    private final Property<TextExpression> textExpression;
    private final Property<Boolean> isscript;
    private final Property<Color> color;

    private final GameObject owner;

    public TextBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        font = new Property<>(Font.class, this, FONT_PROPERTY, Font.DEFAULT_FONT, aEventManager);
        textExpression = new Property<>(TextExpression.class, this, TEXT_EXPRESSION_PROPERTY, new TextExpression(""), aEventManager);
        color = new Property<>(Color.class, this, COLOR_PROPERTY, Color.WHITE, aEventManager);
        isscript = new Property<>(Boolean.class, this, ISSCRIPT_PROPERTY, false, aEventManager);
        owner = aOwner;
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public TextClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public Property<Font> fontProperty() {
        return font;
    }

    @Override
    public Property<TextExpression> textExpressionProperty() {
        return textExpression;
    }

    @Override
    public Property<Color> colorProperty() {
        return color;
    }

    @Override
    public Property<Boolean> isScriptProperty() {
        return isscript;
    }

    @Override
    public TextBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new TextBehavior(aInstance, this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TextBehavior.TYPE_ATTRIBUTE, TextBehavior.TYPE);
        theResult.put(FONT_PROPERTY, font.get().serialize());
        theResult.put(TEXT_EXPRESSION_PROPERTY, textExpression.get().serialize());
        theResult.put(COLOR_PROPERTY, color.get().serialize());
        theResult.put(ISSCRIPT_PROPERTY, Boolean.toString(isscript.get()));
        return theResult;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }

    public static TextBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        TextBehaviorTemplate theTemplate = new TextBehaviorTemplate(aEventManager, aOwner);
        theTemplate.font.setQuietly(Font.deserialize((Map<String, Object>) aSerializedData.get(FONT_PROPERTY)));
        theTemplate.textExpression.setQuietly(TextExpression.deserialize((Map<String, Object>) aSerializedData.get(TEXT_EXPRESSION_PROPERTY)));
        theTemplate.color.setQuietly(Color.deserialize((Map<String, Object>) aSerializedData.get(COLOR_PROPERTY)));
        String theIsScript = (String) aSerializedData.get(ISSCRIPT_PROPERTY);
        if (theIsScript != null) {
            theTemplate.isscript.setQuietly(Boolean.valueOf(theIsScript));
        }
        return theTemplate;
    }
}