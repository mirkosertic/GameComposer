package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;

public interface ContextMenuListener {

    void onDeleteGameScene(GameScene aGameScene);

    void onDeleteGameObject(GameObject aGameObject);

    void onDeleteGameObjectInstance(GameObjectInstance aGameObjectInstance);

    void onCreateNewGameObject(GameScene aGameScene);

    void onCreateNewGameScene();

    void onNewEventSheet(GameScene aGameScene);

    void onDeleteEventSheet(EventSheet aEventSheet);
}
