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
package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class Font implements Distributable {

    public static enum FontName {
        ARIAL, VERDANA
    }

    public static final Font DEFAULT_FONT = new Font(FontName.ARIAL, 10);

    public final FontName name;
    public final int size;

    public Font(FontName aFontName, int aSize) {
        name = aFontName;
        size = aSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Font font = (Font) o;

        if (size != font.size) return false;
        if (name != font.name) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Font changeSize(int aSize) {
        return new Font(name, aSize);
    }

    public Font changeName(FontName aName) {
        return new Font(aName, size);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("name", name.name());
        theResult.put("size", Integer.toString(size));
        return theResult;
    }

    public static Font deserialize(Map<String, Object> aSerializedData) {
        String theFontName = (String) aSerializedData.get("name");
        String theSize = (String) aSerializedData.get("size");
        return new Font(FontName.valueOf(theFontName), Integer.parseInt(theSize));
    }
}
