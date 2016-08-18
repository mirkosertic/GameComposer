package de.mirkosertic.gameengine.web.github;

public class AuthenticationOptions {

    private final String username;
    private final String password;

    public AuthenticationOptions(String aUsername, String aPassword) {
        username = aUsername;
        password = aPassword;
    }

    public AuthenticationOptions() {
        this(null, null);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
