package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.types.Font;
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
}
