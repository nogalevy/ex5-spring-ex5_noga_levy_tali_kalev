package hac.controllers;

import hac.beans.SearchFilter;
import hac.beans.UserSession;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;

//NOGA: change class + file name ?
@Controller
public class Default {
    private final String LIMIT = "6"; //NOGA: change file
    private final String DEFAULT_OFFSET = "0"; //NOGA: change file

    @Autowired
    @Qualifier("searchFilterSession")
    private SearchFilter currSearchFilter = new SearchFilter();

    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserFavouritesService userFavouritesService;

    @GetMapping("/")
    public synchronized String index(Model model) {
        try{
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
        }
        catch (Exception err){
            //TODO:
        }
        return "index";
    }

    @GetMapping("/pages/favourite")
    public synchronized String favourite(Model model) {
        try{
            Long userId = currUserSession.getUserId();
            List<String> categories = JokeApiHandler.getCategoriesFromApi();
            List<Joke> favourites = getUserFavouritesJokes(Integer.parseInt(LIMIT), 0);
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

    @GetMapping("/favourites/get")
    public ResponseEntity<List<Joke>> favourite(@RequestParam(defaultValue = LIMIT) int limit,
                                                @RequestParam(defaultValue = DEFAULT_OFFSET) int offset, Model model) {
        try{
            List<Joke> favourites = getUserFavouritesJokes(limit, offset);
            return ResponseEntity.ok(favourites);
        }
        catch(Exception err ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //NOGA: move to services?
    public List<Joke> getUserFavouritesJokes(int limit, int offset) throws Exception{
        try{
            List<Favourite> favouritesList = userFavouritesService.getUserFavouritesData(limit, offset, currUserSession.getUserId());
            ArrayList<Long> jokeIds = new ArrayList<Long>();
            for(Favourite fav : favouritesList){
                jokeIds.add(fav.getJokeId());
            }
            List<Joke> favourites = JokeApiHandler.getJokesByIdsFromApi(jokeIds);
            return favourites;
        }
        catch (Exception err){
            throw new Exception(err);
        }
    }

    @GetMapping("/pages/userprofile")
    public synchronized String userProfile(Model model) {
        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);

        //retreive user id from user session
        Long userId = currUserSession.getUserId();
        try{
            UserInfo userInfo = userInfoService.getUserById(userId);
            model.addAttribute("firstName", userInfo.getFirstName());
            model.addAttribute("lastName", userInfo.getLastName());
            model.addAttribute("email", userInfo.getEmail());

        }catch (Exception error){
            //todo - handle error
            System.out.println(error.getMessage());
        }
        return "userProfile";
    }

    //NOGA: maybe not need to be here but i needed the same 'currSearchFilter' like in the 'index' method
    //tali: do we need some sort of validation on searchFilter?
    @PostMapping("/pages/search")
    public String search(@ModelAttribute SearchFilter searchFilter, Model model) {
        currSearchFilter.setSelectedCategories(searchFilter.getSelectedCategories());
        currSearchFilter.setSelectedOption(searchFilter.getSelectedOption());

        model.addAttribute("searchFilter", searchFilter);

        return "redirect:/";
    }

    // NOGA: change url path ? not a page
    @GetMapping("/pages/getJokes")
    public synchronized ResponseEntity<String> getJokes() {
        try {
            List<Joke> jokes = JokeApiHandler.getJokesFromApi(currSearchFilter);
            if (jokes == null) {
                String errorResponse = "{\"error\": \"Something happened...no joke at the moment\"}"; //NOGA: final?
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            } else {
                Joke joke = jokes.get(0);
                long userId = currUserSession.getUserId();
                Boolean isFavourite = userFavouritesService.isFavourite(joke.id(), userId);

                Joke responseJoke = new Joke(joke, isFavourite);
                ObjectMapper objectMapper = new ObjectMapper();

                String jokeResponse = objectMapper.writeValueAsString(responseJoke);
                return ResponseEntity.ok(jokeResponse);
            }
        }
        catch (Exception err){
            //TODO:
            err.printStackTrace();
            String errorResponse = "{\"error\": \"Failed to process joke data\"}"; //NOGA: final?
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/pages/logout")
    public String logoutUser(HttpServletRequest request,  RedirectAttributes redirectAttributes){
        request.getSession().invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out successfully");
        return "redirect:/users/login";
    }
}