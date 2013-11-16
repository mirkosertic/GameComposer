package de.mirkosertic.gameengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    public static <T> List<T> asList(T[] aArray) {
        return new ArrayList<T>(Arrays.asList(aArray));
    }
}
