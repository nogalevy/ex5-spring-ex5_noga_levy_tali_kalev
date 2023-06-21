package hac.controllers;

import hac.beans.JokesList;
import hac.beans.SearchFilter;
import hac.beans.UserSession;
import hac.records.Joke;
import hac.records.JokeApiCategoriesResponse;
import hac.records.JokeApiResponse;
import hac.repo.UserInfo;
import hac.repo.UserInfoRepository;
import hac.utils.JokeApiHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class Default {
    @Autowired
    @Qualifier("searchFilterSession")
    private SearchFilter currSearchFilter = new SearchFilter();

    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;
    @Autowired
    private UserInfoRepository userInfoRepository;


    @GetMapping("/")
    public String index(Model model) {
        List<Joke> jokes = JokeApiHandler.getJokesFromApi(currSearchFilter);
//        currSearchFilter.getUri();
        //NOGA: move to function ??
        if (jokes == null) {
            model.addAttribute("joke", "Something happened...no joke at the moment");
        } else {
            Joke joke = jokes.get(0);
//            model.addAttribute("type", joke.type());
//            model.addAttribute("setup", joke.setup());
//            model.addAttribute("delivery", joke.delivery());
//            model.addAttribute("joke", joke.joke());
            model.addAttribute("jokeObj", joke);
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
        List<Joke> favourites = JokeApiHandler.getJokesByIdsFromApi(new ArrayList(Arrays.asList(34, 234, 43)));
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilter", currSearchFilter);
        model.addAttribute("favourites", favourites);
        return "favourite";
    }

    @GetMapping("/pages/userprofile")
    public String userProfile(Model model) {
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
    @PostMapping("/pages/search")
    public String search(@ModelAttribute SearchFilter searchFilter, Model model) {
        System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+="+searchFilter.getSelectedOption());

        currSearchFilter.setSelectedCategories(searchFilter.getSelectedCategories());
        currSearchFilter.setSelectedOption(searchFilter.getSelectedOption());

        model.addAttribute("searchFilter", searchFilter);
//        List<Joke> jokes = JokesList.getJokesFromApi();

        return "redirect:/";
    }

    @GetMapping("/pages/getJokes")
    public ResponseEntity<String> getJokes() {
        List<Joke> jokes = JokeApiHandler.getJokesFromApi(currSearchFilter); //NOGA: i dont knowwwwwwwwww
        if (jokes == null) {
            String errorResponse = "{\"error\": \"Something happened...no joke at the moment\"}";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } else {
            Joke joke = jokes.get(0);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jokeResponse = objectMapper.writeValueAsString(joke);
                return ResponseEntity.ok(jokeResponse);
            } catch (Exception e) {
                e.printStackTrace();
                String errorResponse = "{\"error\": \"Failed to process joke data\"}";
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }

    @PostMapping("/pages/logout")
    public String logoutUser(HttpServletRequest request, Model model){
        //set userSession to logged out
        request.getSession().invalidate();
//        currUserSession.setLoggedIn(false);
//        currUserSession.setUserId(-1);

        System.out.println("logging out user");
        //todo: add message on successful logout
        //redirect to login page
        return "redirect:/users/login";
    }
}