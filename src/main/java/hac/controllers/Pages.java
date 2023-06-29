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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static hac.utils.Constants.LIMIT;

/**
 * Pages controller
 */
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

    /**
     * retrieves joke from joke API and renders it to index page
     * @param model model
     * @return index page
     */
    @GetMapping("/")
    public synchronized String index(Model model) {
        Joke joke = JokeApiHandler.getJokesFromApi(currSearchFilter);

        long userId = currUserSession.getUserId();
        Boolean isFavourite = userFavouritesService.isFavourite(joke.id(), userId);

        Joke responseJoke = new Joke(joke, isFavourite);
        model.addAttribute("jokeObj", responseJoke);

        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);
        return "index";
    }

    /**
     * retrieves user's favourited jokes from joke API and renders it to favourite page
     * @param model model
     * @return favourite page
     */
    @GetMapping("/pages/favourite")
    public synchronized String favourite(Model model) {
        Long userId = currUserSession.getUserId();
        Integer numOfUserFavourites = userFavouritesService.getNumOfUserFavourites(userId);
        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        List<Favourite> favouritesList = userFavouritesService.getUserFavouritesData(Integer.parseInt(LIMIT), 0, currUserSession.getUserId());
        List<Joke> favourites = JokeApiHandler.getUserFavouritesJokes(favouritesList);

        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);
        model.addAttribute("showLoadMoreBtn", numOfUserFavourites > Integer.parseInt(LIMIT));
        model.addAttribute("favourites", favourites);
        return "favourite";
    }

    /**
     * retrieves user's information and renders to user profile page
     * @param model model
     * @return user profile page
     * @throws UserNotFound exception if user is not found in table
     */
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

    /**
     * logs out user and redirects to login page
     * @param request request
     * @param redirectAttributes redirectAttributes
     * @return login page
     */
    @PostMapping("/pages/logout")
    public String logoutUser(HttpServletRequest request,  RedirectAttributes redirectAttributes){
        request.getSession().invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out successfully");
        return "redirect:/users/login";
    }

    /**
     * handles user not found exception
     * @param e exception
     * @param request request
     * @param redirectAttributes redirectAttributes
     * @return login page
     */
    @ExceptionHandler({UserNotFound.class})
    public String handleUserNotFoundExceptions(UserNotFound e,HttpServletRequest request, RedirectAttributes redirectAttributes) {
        request.getSession().invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", e.getMessage() + " please try again later");
        return "redirect:/users/login";
    }

    /**
     * handles all exceptions
     * @param e exception
     * @param redirectAttributes redirectAttributes
     * @return error page
     */
    @ExceptionHandler({Exception.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error","An unexpected error occured: " + e.getMessage());
        return "redirect:/error";
    }
}