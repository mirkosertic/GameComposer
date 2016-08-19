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
package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.type.CollisionPosition;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.GameKeyCode;
import javafx.util.StringConverter;

import javax.inject.Singleton;

@Singleton
public class StringConverterFactory {

    public StringConverter<GameObject> createGameObjectStringConverter() {

        return new StringConverter<GameObject>() {
            @Override
            public String toString(GameObject aValue) {
                return aValue.nameProperty().get();
            }

            @Override
            public GameObject fromString(String aValue) {
                // Nonsense here
                return null;
            }
        };
    }

    public StringConverter<GameKeyCode> createGameKeyCodeStringConverter() {
        return new StringConverter<GameKeyCode>() {
            @Override
            public String toString(GameKeyCode aValue) {
                return aValue.name();
            }

            @Override
            public GameKeyCode fromString(String aValue) {
                return GameKeyCode.valueOf(aValue);
            }
        };
    }

    public StringConverter<Font.FontName> createFontStringConverter() {
        return new StringConverter<Font.FontName>() {
            @Override
            public String toString(Font.FontName aValue) {
                return aValue.name();
            }

            @Override
            public Font.FontName fromString(String aValue) {
                return Font.FontName.valueOf(aValue);
            }
        };
    }

    public StringConverter<Enum> createEnumStringConverter() {
        return new StringConverter<Enum>() {
            @Override
            public String toString(Enum anEnum) {
                return anEnum.name();
            }

            @Override
            public Enum fromString(String s) {
                // nonsense here
                return null;
            }
        };
    }

    public StringConverter<CollisionPosition> createColissionPositiontringConverter() {
        return new StringConverter<CollisionPosition>() {
            @Override
            public String toString(CollisionPosition anEnum) {
                return anEnum.name();
            }

            @Override
            public CollisionPosition fromString(String s) {
                return CollisionPosition.valueOf(s);
            }
        };
    }

}
