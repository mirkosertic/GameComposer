package de.mirkosertic.gamecomposer;

import javafx.scene.input.DataFormat;

import java.io.Serializable;

public class GameObjectClipboardContent implements Serializable {

    public static final DataFormat FORMAT = new DataFormat("GameObject.custom");

    public String gameObjectId;

    public GameObjectClipboardContent() {
    }

    public GameObjectClipboardContent(String aGameObjectId) {
        gameObjectId = aGameObjectId;
    }

    public String getGameObjectId() {
        return gameObjectId;
    }

    public void setGameObjectId(String gameObjectId) {
        this.gameObjectId = gameObjectId;
    }
}
