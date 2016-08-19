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

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.TextExpression;

public interface Text {

    String FONT_PROPERTY = "font";
    String COLOR_PROPERTY = "color";
    String TEXT_EXPRESSION_PROPERTY = "textExpression";
    String ISSCRIPT_PROPERTY = "isScript";

    @ReflectiveField
    Property<Font> fontProperty();

    @ReflectiveField
    Property<TextExpression> textExpressionProperty();

    @ReflectiveField
    Property<Color> colorProperty();

    @ReflectiveField
    Property<Boolean> isScriptProperty();

    @ReflectiveMethod
    void delete();
}