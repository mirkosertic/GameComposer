package de.mirkosertic.sampleplatformer;

import de.mirkosertic.gameengine.core.*;

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
                        theBrick.setPosition(new Position(x * size, y * size));
                        theBrick.setSize(new Size(size, size));
                        theBrick.setName(aBrickGameObject.getName() + "#" + count++);
                        aScene.addGameObjectInstance(theBrick);
                    }
                }
            }
            y++;
        }
    }
}
