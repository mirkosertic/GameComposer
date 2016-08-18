package de.mirkosertic.gameengine.type;

import java.util.Random;

public class UUID {

    private static final Random RANDOM = new Random();

    private UUID() {
    }

    public static String randomUID() {
        return ""+RANDOM.nextLong();
    }
}