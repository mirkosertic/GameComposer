package de.mirkosertic.gameengine.types;

import java.util.Random;

public class UUID {

    private static final Random RANDOM = new Random();

    public static String randomUID() {
        return ""+RANDOM.nextLong();
    }
}
