package hac.records;

public record Joke(String category, String type, String setup, String delivery, String joke, long id, boolean isFavourite, Joke jokeObj) {
    private final static String DEFAULT_JOKE = "some error occurred this is not a joke!";

    public Joke(Joke joke, boolean isFavourite) {
        this(joke.category(), joke.type(), joke.setup(), joke.delivery(), joke.joke(), joke.id(), isFavourite, joke);
    }

    public Joke(){
        this(null, null, null, null,
                DEFAULT_JOKE, 0, true, null);
    }
}