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

import javafx.application.Application;
import javafx.stage.Stage;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class GameComposerApplication extends Application {

    private Weld weld;
    private WeldContainer weldContainer;

    @Override
    public void start(Stage aStage) throws Exception {
        weld = new Weld();
        weldContainer = weld.initialize();
        weldContainer.instance().select(GameComposerFactory.class).get().createAndStart(this, aStage, getParameters());
    }

    @Override
    public void stop() throws Exception {
        weldContainer.event().select(ShutdownEvent.class).fire(new ShutdownEvent());
        super.stop();
        weld.shutdown();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}