package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;

public interface EditorProject {

    TeaVMGameSceneLoader createSceneLoader(TeaVMGameSceneLoader.GameSceneLoadedListener aListener,
            AbstractGameRuntimeFactory aRuntimeFactory);

    TeaVMGameLoader createGameLoader(TeaVMGameLoader.GameLoadedListener aListener);

    TeaVMGameResourceLoader createResourceLoaderFor(String aSceneID);

    void setCurrentPreview(String aSceneDataAsJSON);

    String getPreviewDataAsJSON();
}
