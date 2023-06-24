package hac.controllers;

import hac.OffsetBasedPageRequest;
import hac.beans.SearchFilter;
import hac.beans.UserSession;
import hac.records.Joke;
import hac.repo.Favourite;
import hac.repo.FavouriteRepository;
import hac.repo.UserInfo;
import hac.repo.UserInfoRepository;
import hac.utils.JokeApiHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
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
/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class Default {
    final String LIMIT = "3"; //NOGA: change file
    final String DEFAULT_OFFSET = "0"; //NOGA: change file

    @Autowired
    @Qualifier("searchFilterSession")
    private SearchFilter currSearchFilter = new SearchFilter();

    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private FavouriteRepository favouriteRepository;

    @GetMapping("/")
    public synchronized String index(Model model) {
        List<Joke> jokes = JokeApiHandler.getJokesFromApi(currSearchFilter);
        //NOGA: move to function ??
        if (jokes == null) {
            model.addAttribute("joke", "Something happened...no joke at the moment");
        } else {
            Joke joke = jokes.get(0);
            long userId = currUserSession.getUserId();
            Favourite favourite = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(joke.id(), userId);
            boolean isFavourite = (favourite != null);

            Joke responseJoke = new Joke(joke, isFavourite);
            model.addAttribute("jokeObj", responseJoke);
        }
        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);

        System.out.println(categories);

        return "index";
    }

    @GetMapping("/pages/favourite")
    public String favourite(Model model) {
        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        List<Joke> favourites = getUserFavouritesJokes(Integer.parseInt(LIMIT), 0);
        Integer numOfUserFavourites = favouriteRepository.countFavouritesByUserInfo_Id(currUserSession.getUserId());

        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);
        model.addAttribute("favourites", favourites);
        model.addAttribute("showLoadMoreBtn", numOfUserFavourites > Integer.parseInt(LIMIT));
        return "favourite";
    }

    @GetMapping("/favourites/get")
    public ResponseEntity<List<Joke>> favourite(@RequestParam(defaultValue = LIMIT) int limit,
                                                @RequestParam(defaultValue = DEFAULT_OFFSET) int offset, Model model) {
        List<Joke> favourites = getUserFavouritesJokes(limit, offset);
        return ResponseEntity.ok(favourites);
    }

    public List<Joke> getUserFavouritesJokes(int limit, int offset){
        List<Favourite> favouritesList = getUserFavouritesData(limit, offset);
        ArrayList<Long> jokeIds = new ArrayList<Long>();
        for(Favourite fav : favouritesList){
            System.out.println("@_@_@_@_@_@_@_@_@_@_@_@_@_@_@_@ joke id: " + fav.getJokeId());
            jokeIds.add(fav.getJokeId());
        }
        List<Joke> favourites = JokeApiHandler.getJokesByIdsFromApi(jokeIds);
        return favourites;
    }

    //NOGA: move to services?
    public synchronized List<Favourite> getUserFavouritesData(int limit, int offset) {
        System.out.println("Get all Employees with limit " + limit + " and offset " + offset);
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return favouriteRepository.findFavouritesByUserInfo_Id(currUserSession.getUserId(), pageable);
    }

    @GetMapping("/pages/userprofile")
    public synchronized String userProfile(Model model) {
        //for menu - todo: possible remove
        List<String> categories = JokeApiHandler.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);

        //retreive user id from user session
        Long userId = currUserSession.getUserId();
        //retrieve user first name, last name, email from userinfo repo
        UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
        if(userInfo == null){
            System.out.println("user not found");
        }
        else{
            //add user attributes to model
            model.addAttribute("firstName", userInfo.getFirstName());
            model.addAttribute("lastName", userInfo.getLastName());
            model.addAttribute("email", userInfo.getEmail());
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
        List<Joke> jokes = JokeApiHandler.getJokesFromApi(currSearchFilter); //NOGA: i dont knowwwwwwwwww
        if (jokes == null) {
            String errorResponse = "{\"error\": \"Something happened...no joke at the moment\"}";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } else {
            Joke joke = jokes.get(0);
            long userId = currUserSession.getUserId();
            Favourite favourite = favouriteRepository.getFavouriteByJokeIdAndUserInfo_Id(joke.id(), userId);
            boolean isFavourite = (favourite != null);

            Joke responseJoke = new Joke(joke, isFavourite);
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String jokeResponse = objectMapper.writeValueAsString(responseJoke);
                return ResponseEntity.ok(jokeResponse);
            } catch (Exception e) {
                e.printStackTrace();
                String errorResponse = "{\"error\": \"Failed to process joke data\"}";
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }

    @PostMapping("/pages/logout")
    public String logoutUser(HttpServletRequest request,  RedirectAttributes redirectAttributes){
        //set userSession to logged out
        request.getSession().invalidate();
        System.out.println("logging out user");
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out successfully");
        // Redirect to the login page and pass the logoutMessage attribute
        return "redirect:/users/login";
    }
}