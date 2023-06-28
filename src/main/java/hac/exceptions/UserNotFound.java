package hac.exceptions;

import org.apache.catalina.User;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String message) {
        super(message);
    }
    public UserNotFound() {
        super("User not found");
    }
}
