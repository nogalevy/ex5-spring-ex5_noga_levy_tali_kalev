package hac.records;

/**
 * Joke record contains all information returned from joke API + is favourite flag
 * @param error boolean
 * @param category string
 * @param type string
 * @param setup string
 * @param delivery string
 * @param joke string
 * @param id long
 * @param isFavourite boolean
 * @param jokeObj joke object
 */
public record Joke(boolean error, String category, String type, String setup, String delivery, String joke, long id, boolean isFavourite, Joke jokeObj) {
    private final static String DEFAULT_JOKE = "Some error occurred this is not a joke!";

    public Joke(Joke joke, boolean isFavourite) {
        this(false, joke.category(), joke.type(), joke.setup(), joke.delivery(), joke.joke(), joke.id(), isFavourite, joke);
    }

    public Joke(){
        this(true, null, null, null, null,
                DEFAULT_JOKE, 0, true, null);
    }
}