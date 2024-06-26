package hac.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Save users session data
 */
@Component
public class UserSession implements Serializable {
    private long userId;
    private boolean loggedIn;

    public UserSession() {
        this.loggedIn = false;
    }

    public UserSession(long userId) {
        this.userId = userId;
        this.loggedIn = false;
    }

    // Getters and setters for the members
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
