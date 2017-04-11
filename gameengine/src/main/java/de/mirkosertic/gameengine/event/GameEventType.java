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
package de.mirkosertic.gameengine.event;

public class GameEventType {

    public static final GameEventType CATCH_ALL = new GameEventType("*");

    private final String type;

    public GameEventType(String aType) {
        type = aType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GameEventType that = (GameEventType) o;

        if (!type.equals(that.type))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public String getType() {
        return type;
    }
}
