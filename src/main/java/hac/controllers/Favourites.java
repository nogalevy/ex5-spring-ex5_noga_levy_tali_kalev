package hac.controllers;

import hac.beans.UserSession;
import hac.exceptions.UserNotFound;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * find the user favourites joke ids and fetch them from joke API
     * @param limit num of jokes (default = 6)
     * @param offset num of jokes to skip (default = 0)
     * @return list of jokes instance
     */
    @GetMapping("/get")
    public ResponseEntity<List<Joke>> getFavourites(@RequestParam(defaultValue = LIMIT) int limit,
                                                @RequestParam(defaultValue = DEFAULT_OFFSET) int offset) {
        try{
            List<Favourite> favouritesList = userFavouritesService.getUserFavouritesData(limit, offset, currUserSession.getUserId());
            List<Joke> favourites = JokeApiHandler.getUserFavouritesJokes(favouritesList);
            return ResponseEntity.ok(favourites);
        }
        catch(Exception err ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * saves joke id as favourite of the current user
     * @param jokeId joke id we want to add as user favourite
     * @return jokeId on success else error code
     * @throws Exception
     */
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

    /**
     * delete from favourite the joke id of the current user
     * @param jokeId int
     * @return jokeId on success else error code
     */
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

    /**
     * count number of user favourites
     * @return number of favourites on success else error code
     */
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
//    @ExceptionHandler({UserNotFound.class})
//    public String handleUserNotFoundExceptions(UserNotFound e, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("error", e.getMessage());
//        return "redirect:/users/login";
//    }
}
