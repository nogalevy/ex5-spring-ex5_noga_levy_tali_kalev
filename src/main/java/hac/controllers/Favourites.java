package hac.controllers;

import hac.beans.UserSession;
import hac.exceptions.UserNotFound;
import hac.records.Joke;
import hac.repo.Favourite;
import hac.services.UserFavouritesService;
import hac.utils.JokeApiHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hac.utils.Constants.DEFAULT_OFFSET;
import static hac.utils.Constants.LIMIT;

/**
 * Favourites controller
 */
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
                                                @RequestParam(defaultValue = DEFAULT_OFFSET) int offset){
        List<Favourite> favouritesList = userFavouritesService.getUserFavouritesData(limit, offset, currUserSession.getUserId());
        List<Joke> favourites = JokeApiHandler.getUserFavouritesJokes(favouritesList);
        return ResponseEntity.ok(favourites);
    }

    /**
     * saves joke id as favourite of the current user
     * @param jokeId joke id we want to add as user favourite
     * @return jokeId on success else error code
     * @throws Exception if joke id is already in user favourites or user id is not found
     */
    @PostMapping("/add")
    public synchronized ResponseEntity<Long> addUserFavourite(@RequestBody Long jokeId) throws Exception {
        long userId = currUserSession.getUserId();
        userFavouritesService.saveUserFavourite(jokeId, userId);
        return ResponseEntity.ok(jokeId);
    }

    /**
     * delete from favourite the joke id of the current user
     * @param jokeId int
     * @return jokeId on success else error code
     * @throws Exception if joke id is not in user favourites or user id is not found
     */
    @PostMapping("/delete")
    public synchronized ResponseEntity<Long> deleteUserFavourite(@RequestBody Long jokeId) throws Exception{
        long userId = currUserSession.getUserId();
        userFavouritesService.deleteUserFavourite(jokeId, userId);
        return ResponseEntity.ok(jokeId);
    }

    /**
     * count number of user favourites
     * @return number of favourites on success else error code
     */
    @GetMapping("/count")
    public synchronized ResponseEntity<Integer> countUserFavourites() {
        long userId = currUserSession.getUserId();
        Integer numOfUserFavourites = userFavouritesService.getNumOfUserFavourites(userId);
        return ResponseEntity.ok(numOfUserFavourites);
    }

    /**
     * Handles user not found exception
     * @param e exception
     * @param request http request
     * @return error message
     */
    @ExceptionHandler({UserNotFound.class})
    public ResponseEntity<String> handleUserNotFoundExceptions(UserNotFound e, HttpServletRequest request) {
        request.getSession().invalidate(); //ends session -> signs out user
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Handles all other exceptions
     * @param e exception
     * @return error message
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleValidationExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
