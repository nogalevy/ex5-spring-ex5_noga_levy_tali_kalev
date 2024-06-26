package hac.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hac.beans.SearchFilter;
import hac.beans.UserSession;
import hac.records.Joke;
import hac.services.UserFavouritesService;
import hac.utils.JokeApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Jokes controller
 */
@Controller
@RequestMapping(value = "/api")
public class Jokes {
    @Autowired
    @Qualifier("searchFilterSession")
    private SearchFilter currSearchFilter;

    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserFavouritesService userFavouritesService;

    /**
     * get joke from api
     * @return joke object as string on success else error code
     */
    @GetMapping("/getJokes")
    public synchronized ResponseEntity<String> getJokes() {
        try {
            Joke joke = JokeApiHandler.getJokesFromApi(currSearchFilter);
            long userId = currUserSession.getUserId();
            Boolean isFavourite = userFavouritesService.isFavourite(joke.id(), userId);

            Joke responseJoke = new Joke(joke, isFavourite);
            ObjectMapper objectMapper = new ObjectMapper();

            String jokeResponse = objectMapper.writeValueAsString(responseJoke);
            return ResponseEntity.ok(jokeResponse);
        }
        catch (Exception err){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err.getMessage());
        }
    }

    /**
     * set the search filter object
     * @param searchFilter search form inputs
     * @param model model
     * @return redirect to '/'
     */
    @PostMapping("/search")
    public String search(@ModelAttribute SearchFilter searchFilter, Model model) {
        currSearchFilter.setSelectedCategories(searchFilter.getSelectedCategories());
        currSearchFilter.setSelectedOption(searchFilter.getSelectedOption());

        model.addAttribute("searchFilter", searchFilter);

        return "redirect:/";
    }
}
