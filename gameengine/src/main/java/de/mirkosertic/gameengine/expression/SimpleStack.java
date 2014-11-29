package de.mirkosertic.gameengine.expression;

import java.util.ArrayList;

public class SimpleStack<T> extends ArrayList<T> {

    public T peek() {
        int len = size();

        if (len == 0)  {
            throw new EmptyStackException();
        }
        return get(len - 1);
    }

    public T pop() {
        return remove(size() - 1);
    }

    public T push(T aToken) {
        add(aToken);
        return aToken;
    }
}
