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

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class Spritesheet {

    private final static String NAME_PROPERTY = "name";
    private final static String JSONFILE_PROPERTY = "jsonfile";

    private final Property<String> nameProperty;
    private final Property<ResourceName> jsonFileProperty;

    public Spritesheet(GameScene aGameScene) {

        GameEventManager theEventManager = aGameScene.getRuntime().getEventManager();

        nameProperty = new Property<>(String.class, this, NAME_PROPERTY, theEventManager);
        jsonFileProperty = new Property<>(ResourceName.class, this, JSONFILE_PROPERTY, theEventManager);
    }

    public Property<String> nameProperty() {
        return nameProperty;
    }

    public Property<ResourceName> jsonFileProperty() {
        return jsonFileProperty;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(NAME_PROPERTY, nameProperty.get());
        ResourceName theResourceName = jsonFileProperty.get();
        theResult.put(JSONFILE_PROPERTY, theResourceName.name);
        return theResult;
    }

    public static Spritesheet unmarshall(GameScene aScene, Map<String, Object> aData) {
        Spritesheet theSeet = new Spritesheet(aScene);
        theSeet.nameProperty.setQuietly((String) aData.get(NAME_PROPERTY));
        String theJSONFile = (String) aData.get(JSONFILE_PROPERTY);
        if (theJSONFile != null) {
            theSeet.jsonFileProperty.setQuietly(new ResourceName(theJSONFile));
        }
        return theSeet;
    }
}