package hac.controllers;

import hac.beans.UserSession;
import hac.records.Joke;
import hac.repo.Favourite;
import hac.services.UserFavouritesService;
import hac.utils.JokeApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

import static hac.utils.Constants.DEFAULT_OFFSET;
import static hac.utils.Constants.LIMIT;

@Controller
@RequestMapping(value = "/api/favourites")
public class Favourites {
    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserFavouritesService userFavouritesService;

    @GetMapping("/get")
    public ResponseEntity<List<Joke>> favourite(@RequestParam(defaultValue = LIMIT) int limit,
                                                @RequestParam(defaultValue = DEFAULT_OFFSET) int offset, Model model) {
        try{
            List<Favourite> favouritesList = userFavouritesService.getUserFavouritesData(limit, offset, currUserSession.getUserId());
            List<Joke> favourites = JokeApiHandler.getUserFavouritesJokes(favouritesList);
            return ResponseEntity.ok(favourites);
        }
        catch(Exception err ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public synchronized ResponseEntity<Long> addUserFavourite(@RequestBody Long jokeId) throws Exception {
        try {
            long userId = currUserSession.getUserId();
            userFavouritesService.saveUserFavourite(jokeId, userId);
            return ResponseEntity.ok(jokeId);
        }
        catch (Exception err){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/delete")
    public synchronized ResponseEntity<Long> deleteUserFavourite(@RequestBody Long jokeId) {
        try {
            long userId = currUserSession.getUserId();
            userFavouritesService.deleteUserFavourite(jokeId, userId);
            return ResponseEntity.ok(jokeId);

        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //TODO : throw error?
    @GetMapping("/count")
    public synchronized ResponseEntity<Integer> countUserFavourites() {
        try{
            long userId = currUserSession.getUserId();
            Integer numOfUserFavourites = userFavouritesService.getNumOfUserFavourites(userId);
            return ResponseEntity.ok(numOfUserFavourites);
        }
        catch (Exception err){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }

    //todo: return ResponseEntity not page
    @ExceptionHandler({Exception.class})
    public String handleValidationExceptions(Exception ex, Model model) {
        // we can insert the message into the model
        System.out.println("Error: " + ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
