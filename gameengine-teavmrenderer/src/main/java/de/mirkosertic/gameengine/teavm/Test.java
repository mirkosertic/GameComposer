package de.mirkosertic.gameengine.teavm;

import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by mirkosertic on 26.11.2014.
 */
public class Test {

    public static void main(String[] args) throws IOException, ParseException {
        Map o = (Map) JSONValue.parseWithException(new FileReader("C:/source/idea_projects/GameEngine/gameengine-teavmrenderer/target/gameengine-teavmrenderer-0.2-SNAPSHOT/scene1/scene.json"));
        System.out.println(o.toString());
    }
}
