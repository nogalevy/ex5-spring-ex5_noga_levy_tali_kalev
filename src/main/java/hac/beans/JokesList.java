package hac.beans;

import java.util.ArrayList;

//TALI: CURRENTLY UNUSED
public class JokesList {
    private ArrayList<Joke> jokesList = new ArrayList<>();

    public JokesList() {
    }

    public ArrayList<Joke> getJokesList() {
        return jokesList;
    }

    public void setJokesList(ArrayList<Joke> jokes) {
        this.jokesList = jokes;
    }

    public void add (Joke joke) {
        jokesList.add(joke);
    }

    public void clear() {
        jokesList.clear();
    }
}
