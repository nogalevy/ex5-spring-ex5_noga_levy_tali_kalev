package hac.controllers;

import hac.beans.SearchFilter;
import hac.beans.UserSession;
import hac.exceptions.UserNotFound;
import hac.records.Joke;
import hac.repo.Favourite;
import hac.repo.UserInfo;
import hac.services.UserFavouritesService;
import hac.services.UserInfoService;
import hac.utils.JokeApiHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static hac.utils.Constants.LIMIT;

//NOGA: change class + file name ?
@Controller
public class Pages {
    @Autowired
    @Qualifier("searchFilterSession")
    private SearchFilter currSearchFilter;

    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserFavouritesService userFavouritesService;

    @GetMapping("/")
    public synchronized String index(Model model) {
        List<Joke> jokes = JokeApiHandler.getJokesFromApi(currSearchFilter);
        if (jokes == null) {
            model.addAttribute("joke", "Something happened...no joke at the moment");
        } else {
            Joke joke = jokes.get(0);
            long userId = currUserSession.getUserId();
            Boolean isFavourite = userFavouritesService.isFavourite(joke.id(), userId);

            Joke responseJoke = new Joke(joke, isFavourite);
            model.addAttribute("jokeObj", responseJoke);
        }
        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);
        return "index";
    }

    @GetMapping("/pages/favourite")
    public synchronized String favourite(Model model) {
        try{
            Long userId = currUserSession.getUserId();
            List<String> categories = JokeApiHandler.getCategoriesFromApi();
            List<Favourite> favouritesList = userFavouritesService.getUserFavouritesData(Integer.parseInt(LIMIT), 0, currUserSession.getUserId());
            List<Joke> favourites = JokeApiHandler.getUserFavouritesJokes(favouritesList);

            Integer numOfUserFavourites = userFavouritesService.getNumOfUserFavourites(userId);

            model.addAttribute("categories", categories);
            model.addAttribute("searchFilter", currSearchFilter);
            model.addAttribute("favourites", favourites);
            model.addAttribute("showLoadMoreBtn", numOfUserFavourites > Integer.parseInt(LIMIT));
        }
        catch (Exception err){
            //TODO:
        }
        return "favourite";
    }

    @GetMapping("/pages/userprofile")
    public synchronized String userProfile(Model model) throws UserNotFound {
        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);

        //retreive user id from user session
        Long userId = currUserSession.getUserId();
        UserInfo userInfo = userInfoService.getUserById(userId);
        model.addAttribute("firstName", userInfo.getFirstName());
        model.addAttribute("lastName", userInfo.getLastName());
        model.addAttribute("email", userInfo.getEmail());

        return "userProfile";
    }

    @PostMapping("/pages/logout")
    public String logoutUser(HttpServletRequest request,  RedirectAttributes redirectAttributes){
        request.getSession().invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out successfully");
        return "redirect:/users/login";
    }

    @ExceptionHandler({UserNotFound.class})
    public String handleUserNotFoundExceptions(UserNotFound e,HttpServletRequest request, RedirectAttributes redirectAttributes) {
        request.getSession().invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", e.getMessage() + " please try again later");
        return "redirect:/users/login";
    }

    @ExceptionHandler({Exception.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error","An unexpected error occured: " + e.getMessage());
        return "redirect:/error";
    }
}