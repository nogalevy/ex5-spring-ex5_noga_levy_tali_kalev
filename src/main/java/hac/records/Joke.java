package hac.records;

public record Joke(String category, String type, String setup, String delivery, String joke, long id, boolean isFavourite, Joke jokeObj) {

    public Joke(Joke joke, boolean isFavourite) {
        this(joke.category(), joke.type(), joke.setup(), joke.delivery(), joke.joke(), joke.id(), isFavourite, joke);
    }
}