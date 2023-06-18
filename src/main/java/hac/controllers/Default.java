package hac.controllers;

import hac.beans.JokesList;
import hac.records.Joke;
import hac.records.JokeApiCategoriesResponse;
import hac.records.JokeApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;


import java.util.Collections;
import java.util.List;

/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class Default {

    @GetMapping("/")
    public String index(Model model) {
        List<Joke> jokes = JokesList.getJokesFromApi();
        if (jokes == null) {
            model.addAttribute("joke", "Something happened...no joke at the moment");
        } else {
            Joke joke = jokes.get(0);
            model.addAttribute("type", joke.type());
            model.addAttribute("setup", joke.setup());
            model.addAttribute("delivery", joke.delivery());
            model.addAttribute("joke", joke.joke());
        }
        List<String> categories = JokesList.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        System.out.println(categories);

        return "index";
    }

    @GetMapping("/favourite")
    public String favourite(Model model) {
        List<String> categories = JokesList.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        return "favourite";
    }

    @GetMapping("/user")
    public String userProfile(Model model) {
        List<String> categories = JokesList.getCategoriesFromApi();
        model.addAttribute("categories", categories);
        return "userProfile";
    }
}