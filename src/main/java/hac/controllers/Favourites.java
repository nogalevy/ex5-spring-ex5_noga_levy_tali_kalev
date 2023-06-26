package hac.controllers;

import hac.beans.UserSession;
import hac.repo.FavouriteRepository;
import hac.repo.UserInfoRepository;
import hac.services.UserFavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;

@Controller
public class Favourites {
    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserFavouritesService userFavouritesService;

    @PostMapping("/favourites/add")
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

    @PostMapping("/favourites/delete")
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
    @GetMapping("/favourites/count")
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
