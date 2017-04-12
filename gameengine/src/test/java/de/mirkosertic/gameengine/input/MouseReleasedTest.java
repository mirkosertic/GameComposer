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
package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.Position;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MouseReleasedTest {

    @Test
    public void testCreate() {
        GameObjectInstance[] theInstances = new GameObjectInstance[0];
        Position thePosition = new Position(10, 20);
        MouseReleased theEvent = new MouseReleased(thePosition, theInstances);

        assertSame(thePosition, theEvent.position);
        assertSame(theInstances, theEvent.affectedInstances);
        assertEquals("MouseReleased", theEvent.type.getType());
    }
}