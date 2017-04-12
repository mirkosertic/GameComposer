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

public class GameObjectConfigurationChangedTest {

    @Test
    public void testInit() {
        GameObject theObject = mock(GameObject.class);
        GameObjectConfigurationChanged theEvent = new GameObjectConfigurationChanged(theObject);
        assertSame(theObject, theEvent.object);
        assertEquals("GameObjectConfigurationChanged", theEvent.getType().getType());
    }

}