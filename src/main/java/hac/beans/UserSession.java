package hac.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UserSession implements Serializable {
    private long user_id;
    private boolean loggedIn;

public UserSession() {
        this.loggedIn = false;
    }

    public UserSession(long user_id) {
        this.user_id = user_id;
        this.loggedIn = false;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
