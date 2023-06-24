package hac.records;

import java.util.List;

public record JokeApiResponse(boolean error, int amount, List<Joke> jokes, String category, String type, String setup, String delivery, String joke, long id, boolean isFavourite) {
    public List<Joke> jokes() {
        return jokes != null ? jokes : List.of(new Joke(category, type, setup, delivery, joke, id, isFavourite, null));
    }
}
