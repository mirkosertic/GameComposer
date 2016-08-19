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
package de.mirkosertic.gameengine.generic;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;

import java.util.HashMap;
import java.util.Map;

public class CSSCache {

    private final Map<Font, String> fontCache;
    private final Map<Color, String> colorCache;

    public CSSCache() {
        fontCache = new HashMap<>();
        colorCache = new HashMap<>();
    }

    public String toFont(Font aFont) {
        String theCSS = fontCache.get(aFont);
        if (theCSS == null) {
            theCSS = CSSUtils.toFont(aFont);
            fontCache.put(aFont, theCSS);
        }
        return theCSS;
    }

    public String toColor(Color aColor) {
        String theCSS = colorCache.get(aColor);
        if (theCSS == null) {
            theCSS = CSSUtils.toColor(aColor);
            colorCache.put(aColor, theCSS);
        }
        return theCSS;
    }
}