package de.mirkosertic.gameengine.web.github;

public interface FileVisitor {

    void visit(File aFile, int aStatuscode, String aETag);
}
