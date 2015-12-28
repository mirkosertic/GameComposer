package java.io;

public class StringReader extends Reader {

    private String str;

    private int markpos = -1;

    private int pos;

    private int count;

    public StringReader(String str) {
        this.str = str;
        this.count = str.length();
    }

    @Override
    public void close() {
        if (isOpen()) {
            str = null;
        }
    }

    private boolean isOpen() {
        return str != null;
    }

    @Override
    public void mark(int readLimit) throws IOException {
        if (readLimit >= 0) {
            if (isOpen()) {
                markpos = pos;
            } else {
                throw new IOException(); //$NON-NLS-1$
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() throws IOException {
        if (isOpen()) {
            if (pos != count) {
                return str.charAt(pos++);
            }
            return -1;
        }
        throw new IOException(); //$NON-NLS-1$
    }

    @Override
    public int read(char buf[], int offset, int len) throws IOException {
        // avoid int overflow
        if (0 <= offset && offset <= buf.length && 0 <= len && len <= buf.length - offset) {
            if (isOpen()) {
                if (pos == this.count) {
                    return -1;
                }
                int end = pos + len > this.count ? this.count : pos + len;
                str.getChars(pos, end, buf, offset);
                int read = end - pos;
                pos = end;
                return read;
            }
            throw new IOException(); //$NON-NLS-1$
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public boolean ready() throws IOException {
        if (isOpen()) {
            return true;
        }
        throw new IOException(); //$NON-NLS-1$
    }

    @Override
    public void reset() throws IOException {
        if (isOpen()) {
            pos = markpos != -1 ? markpos : 0;
        } else {
            throw new IOException(); //$NON-NLS-1$
        }
    }

    @Override
    public long skip(long ns) throws IOException {
        if (isOpen()) {
            if (ns <= 0) {
                return 0;
            }
            long skipped = 0;
            if (ns < this.count - pos) {
                pos = pos + (int) ns;
                skipped = ns;
            } else {
                skipped = this.count - pos;
                pos = this.count;
            }
            return skipped;
        }
        throw new IOException(); //$NON-NLS-1$
    }
}
