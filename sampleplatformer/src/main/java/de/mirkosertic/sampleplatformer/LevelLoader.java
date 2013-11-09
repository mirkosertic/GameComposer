package de.mirkosertic.sampleplatformer;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameObjectInstanceFactory;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

import java.io.BufferedReader;
import java.io.IOException;

public class LevelLoader {

    public void loadFrom(BufferedReader aReader, GameObject aBrickGameObject, GameObjectInstanceFactory aInstanceFactory, GameScene aScene) throws IOException {
        int y = 0;
        int size = 76;
        int count = 1;
        while (aReader.ready()) {
            String theLine = aReader.readLine();
            if (theLine != null) {
                for (int x = 0; x < theLine.length(); x++) {
                    if (theLine.charAt(x) == 'X') {
                        GameObjectInstance theBrick = aInstanceFactory.createFrom(aBrickGameObject);
                        theBrick.positionProperty().setQuietly(new Position(x * size, y * size));
                        theBrick.getOwnerGameObject().sizeProperty().setQuietly(new Size(size, size));
                        theBrick.nameProperty().setQuietly(aBrickGameObject.nameProperty().getName() + "#" + count++);
                        aScene.addGameObjectInstance(theBrick);
                    }
                }
            }
            y++;
        }
    }
}
