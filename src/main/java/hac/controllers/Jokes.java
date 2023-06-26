package hac.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hac.beans.SearchFilter;
import hac.beans.UserSession;
import hac.records.Joke;
import hac.repo.Favourite;
import hac.services.UserFavouritesService;
import hac.services.UserFavouritesServiceImpl;
import hac.utils.JokeApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Jokes {
    @Autowired
    @Qualifier("searchFilterSession")
    private SearchFilter currSearchFilter;

    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserFavouritesService userFavouritesService;

    // NOGA: change url path ? not a page maybe api/....
    @GetMapping("/pages/getJokes")
    public synchronized ResponseEntity<String> getJokes() {
        try {
            List<Joke> jokes = JokeApiHandler.getJokesFromApi(currSearchFilter);
            Joke joke = jokes.get(0);
            long userId = currUserSession.getUserId();
            Boolean isFavourite = userFavouritesService.isFavourite(joke.id(), userId);

            Joke responseJoke = new Joke(joke, isFavourite);
            ObjectMapper objectMapper = new ObjectMapper();

            String jokeResponse = objectMapper.writeValueAsString(responseJoke);
            return ResponseEntity.ok(jokeResponse);
        }
        catch (Exception err){
            //TODO:
            err.printStackTrace();
            String errorResponse = "{\"error\": \"Failed to process joke data\"}"; //NOGA: final?
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
