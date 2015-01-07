package java.lang.ref;

public abstract class Reference<T> {

    private T referent;

    public Reference() {
    }

    public Reference(T aReferent) {
        referent = aReferent;
    }

    public T get() {
        return referent;
    }
}
