package hac.controllers;

import hac.beans.UserSession;
import hac.repo.Favourite;
import hac.repo.FavouriteRepository;
import hac.repo.UserInfo;
import hac.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;

@Controller
public class Favourites {
    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @PostMapping("/favourites/add")
    public synchronized ResponseEntity<Long> addUserFavourite(@RequestBody Long jokeId) throws Exception {
        Favourite newFavourite = new Favourite(jokeId);
        long userId = currUserSession.getUserId();

        Favourite favourite = userInfoRepository.findById(userId).map(user -> {
            newFavourite.setUserInfo(user);
            return favouriteRepository.save(newFavourite);
        }).orElseThrow(() -> new Exception("sorry" ));

        return ResponseEntity.ok(jokeId);
    }

    @ExceptionHandler({Exception.class})
    public String handleValidationExceptions(Exception ex, Model model) {
        // we can insert the message into the model
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
