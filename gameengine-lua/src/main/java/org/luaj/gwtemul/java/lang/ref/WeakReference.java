package java.lang.ref;

public class WeakReference<T> extends Reference<T> {

    public WeakReference(T aReferent) {
        super(aReferent);
    }
}