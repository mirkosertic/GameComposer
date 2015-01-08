package java.io;

public abstract class Reader {

    public boolean ready() throws IOException {
        return false;
    }

    public int read() throws IOException {
        throw new IOException();
    }

    public int read(char[] aBuffer) throws IOException {
        throw new IOException();
    }

    public int read(char buf[], int offset, int len) throws IOException {
        throw new IOException();
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readLimit) throws IOException {
    }

    public void close() throws IOException {
    }

    public void reset() throws IOException {
    }

    public long skip(long ns) throws IOException {
        throw new IOException();
    }
}