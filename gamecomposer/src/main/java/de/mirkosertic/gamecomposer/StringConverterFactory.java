package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameKeyCode;
import de.mirkosertic.gameengine.core.GameObject;
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
}
