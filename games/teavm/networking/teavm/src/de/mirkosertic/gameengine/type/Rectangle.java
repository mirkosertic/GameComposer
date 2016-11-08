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

public class Rectangle implements Distributable {

    public final Position position;
    public final Size size;

    public Rectangle() {
        this(new Position(), new Size(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public Rectangle(Position aPosition, Size aSize) {
        position = aPosition;
        size = aSize;
    }

    public boolean contains(Position aPosition) {
        return aPosition.x >= position.x &&
                aPosition.y >= position.y &&
                aPosition.x <= position.x + size.width &&
                aPosition.y <= position.y + size.height;
    }

    public Rectangle newPosition(Position aNewPosition) {
        return new Rectangle(aNewPosition, size);
    }

    public Rectangle newSize(Size aNewSize) {
        return new Rectangle(position, aNewSize);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("position", position.serialize());
        theResult.put("size", size.serialize());
        return theResult;
    }

    public static Rectangle deserialize(Map<String, Object> aSerializedData) {
        Position thePosition = Position.deserialize((Map<String, Object>) aSerializedData.get("position"));
        Size theSize = Size.deserialize((Map<String, Object>) aSerializedData.get("size"));
        return new Rectangle(thePosition, theSize);
    }
}