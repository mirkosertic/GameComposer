package de.mirkosertic.gameengine.web.github;

import de.mirkosertic.gameengine.teavm.TeaVMMap;
import org.teavm.jso.core.JSArray;

public class Repository {

    private final Github github;
    private final String user;
    private final String name;

    public Repository(Github aGithub, String aUser, String aName) {
        github = aGithub;
        user = aUser;
        name = aName;
    }

    public void visitContent(FileVisitor aVisitor) {
        github.open("/repos/" + user + "/" + name + "/contents", (aObject, aStatusCode, aResponseETag) -> {
            if (TeaVMMap.isArray(aObject)) {
                JSArray<File> theArray = aObject.cast();
                for (int i = 0; i < theArray.getLength(); i++) {
                    aVisitor.visit(theArray.get(i), aStatusCode, aResponseETag);
                }
            } else {
                aVisitor.visit((File) aObject, aStatusCode, aResponseETag);
            }
        }, true, null);
    }

    public void visitContent(File aFile, FileVisitor aVisitor, String aETag) {
        github.open(aFile.getUrl(), (aObject, aStatusCode, aResponseETag) -> {
            if (TeaVMMap.isArray(aObject)) {
                JSArray<File> theArray = aObject.cast();
                for (int i = 0; i < theArray.getLength(); i++) {
                    aVisitor.visit(theArray.get(i), aStatusCode, aResponseETag);
                }
            } else {
                aVisitor.visit((File) aObject, aStatusCode, aResponseETag);
            }
        }, false, aETag);
    }
}
