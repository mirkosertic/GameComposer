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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.deleteinstance;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.DeleteGameObjectInstanceAction;
import javafx.scene.Node;

import javax.inject.Inject;

public class DeleteInstanceEditorController implements ActionController {

    @Inject
    PersistenceManager persistenceManager;

    private Node view;
    private DeleteGameObjectInstanceAction action;

    DeleteInstanceEditorController initialize(Node aView, DeleteGameObjectInstanceAction aAction) {
        view = aView;
        action = aAction;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}