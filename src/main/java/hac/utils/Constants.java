package hac.utils;

/**
 * Constants class
 */
public final class Constants {
    private Constants(){}

    // Joke API constants for retrieving favourites
    public static final String LIMIT = "6";
    public static final String DEFAULT_OFFSET = "0";

    // Initial search filter chosen option for retrieving from joke API
    public static final String INITIAL_SEARCH_FILTER = "Any";

    // Validation messages
    public static final String FIRST_NAME_MANDATORY = "First name is mandatory";
    public static final String LAST_NAME_MANDATORY = "Last name is mandatory";
    public static final String EMAIL_MANDATORY = "Email is mandatory";
    public static final String EMAIL_NOT_VALID = "Email is not valid";
    public static final String PASSWORD_MANDATORY = "Password is mandatory";
    public static final String JOKE_ID_MANDATORY = "Joke id is mandatory";
    public static final String STRING_LENGTH_2_TO_30 = "Must be between 2 and 30 characters";

    // general exception messages
    public static final String FAVOURITE_DATA_ERROR_MSG = "Cannot find favourite with the given jokeId and userId";
    public static final String FAVOURITE_EXIST_ERROR_MSG = "favourite already exist";
    public static final String USER_EMAIL_EXIST_ERROR_MSG = "There is already an account registered with that email";
    public static final String USER_INVALID_LOGIN_ERROR_MSG = "Invalid email or password";

}
