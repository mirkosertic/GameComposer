package de.mirkosertic.gameengine.web.github;

import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.json.JSON;

public class Github {

    @JSBody(params = {"aString"}, script = "return aString;")
    public static native String btoa(String aString);

    public interface ResponseHandler {
        void handle(JSObject aObject, int aReturnCode, String aETag);
    }

    private final AuthenticationOptions authenticationOptions;

    public Github(AuthenticationOptions aOptions) {
        authenticationOptions = aOptions;
    }

    public Repository getRepo(String aUser, String aName) {
        return new Repository(this, aUser, aName);
    }

    void open(String aPath, ResponseHandler aHandler, boolean aPrefixWithAPI, String aETag) {
        final XMLHttpRequest theRequest = XMLHttpRequest.create();
        if (aETag != null) {
            theRequest.setRequestHeader("If-None-Match", aETag);
        }
        theRequest.onComplete(() -> aHandler.handle(JSON.parse(theRequest.getResponseText()), theRequest.getStatus(), theRequest.getResponseHeader("ETag")));
        if (authenticationOptions.getUsername() != null) {
            TeaVMLogger.info("Requesting " + aPath + " authorized with " + aPrefixWithAPI);
            theRequest.open("GET", (aPrefixWithAPI ? "https://api.github.com" : "") + aPath, true, authenticationOptions.getUsername(), authenticationOptions.getPassword());
        } else {
            TeaVMLogger.info("Requesting " + aPath + " unauthorized with " + aPrefixWithAPI);
            theRequest.open("GET", (aPrefixWithAPI ? "https://api.github.com" : "") + aPath, true);
        }
        theRequest.send();
    }
}