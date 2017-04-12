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
package de.mirkosertic.gameengine.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class GameObjectInstanceAddedToSceneTest {

    @Test
    public void testInit() {
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        GameObjectInstanceAddedToScene theEvent = new GameObjectInstanceAddedToScene(theInstance);
        assertSame(theInstance, theEvent.instance);
        assertEquals("GameObjectInstanceAddedToScene", theEvent.getType().getType());
    }

}