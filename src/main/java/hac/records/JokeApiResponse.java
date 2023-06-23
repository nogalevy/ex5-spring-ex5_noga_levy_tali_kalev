package hac.records;

import java.util.List;

public record JokeApiResponse(boolean error, int amount, List<Joke> jokes, String category, String type, String setup, String delivery, String joke, long id, boolean isFavourite) {
    public List<Joke> jokes() {
        return jokes != null ? jokes : List.of(new Joke(category, type, setup, delivery, joke, id, isFavourite, null));
    }
}

//public class JokeApiResponse {
//    private boolean error;
//    private int amount;
//    private List<Joke> jokes;
//
//    public JokeApiResponse() {
//    }
//
//    public JokeApiResponse(boolean error, int amount, List<Joke> jokes) {
//        this.error = error;
//        this.amount = amount;
//        this.jokes = jokes;
//    }
//
//    public boolean isError() {
//        return error;
//    }
//
//    public void setError(boolean error) {
//        this.error = error;
//    }
//
//    public int getAmount() {
//        return amount;
//    }
//
//    public void setAmount(int amount) {
//        this.amount = amount;
//    }
//
//    public List<Joke> getJokes() {
//        return jokes;
//    }
//
//    public void setJokes(List<Joke> jokes) {
//        this.jokes = jokes;
//    }
//}