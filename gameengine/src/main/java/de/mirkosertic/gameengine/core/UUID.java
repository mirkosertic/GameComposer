package de.mirkosertic.gameengine.core;

import java.util.Random;

public class UUID {

    private static Random RANDOM = new Random();

    public static String randomUID() {
        return ""+RANDOM.nextLong();
    }
}
