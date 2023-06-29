package hac.exceptions;

/**
 * User not found exception
 */
public class UserNotFound extends RuntimeException{
    public UserNotFound(String message) {
        super(message);
    }
    public UserNotFound() {
        super("User not found");
    }
}
