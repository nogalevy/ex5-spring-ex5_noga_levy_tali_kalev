package hac.controllers;

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
        List<Joke> jokes = getJokesFromApi();
        if (jokes == null) {
            model.addAttribute("joke", "Something happened...no joke at the moment");
        } else {
            Joke joke = jokes.get(0);
            model.addAttribute("type", joke.type());
            model.addAttribute("setup", joke.setup());
            model.addAttribute("delivery", joke.delivery());
            model.addAttribute("joke", joke.joke());
        }
        List<String> categories = getCategoriesFromApi();
        model.addAttribute("categories", categories);
        System.out.println(categories);

        return "index";
    }

    @GetMapping("/favourite")
    public String favourite(Model model) {
        return "favourite";
    }

    @GetMapping("/user")
    public String userProfile(Model model) {
        return "userProfile";
    }

        @GetMapping("/getJokes")
        public ResponseEntity<String> getJokes() {
            List<Joke> jokes = getJokesFromApi();
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


//============================GET FROM API========================================
    private List<Joke> getJokesFromApi(){
//        final String uri = "https://v2.jokeapi.dev/joke/Any?amount=4?format=json";
//        final String uri = "https://v2.jokeapi.dev/joke/Any";
//        final String uri = "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&amount=2";
        final String uri = "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
        RestTemplate restTemplate = new RestTemplate();
        List<Joke> jokes = null;

        ResponseEntity<JokeApiResponse> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                JokeApiResponse.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            JokeApiResponse jokeApiResponse = responseEntity.getBody();
            System.out.println(jokeApiResponse.error());
            System.out.println(jokeApiResponse);
            if (jokeApiResponse != null && !jokeApiResponse.error()) {
                jokes = jokeApiResponse.jokes();
                if (jokes != null) {
                    for (Joke joke : jokes) {
                        if(joke.joke() != null){
                            System.out.println("Printing one liner");
                            System.out.println(joke.joke());
                        }
                        else{
                            System.out.println("Printing two parter");
                            System.out.println(joke.setup());
                            System.out.println(joke.delivery());
                        }
                        System.out.println("-----");
                    }
                } else {
                    System.out.println("No jokes found.");
                }
            } else {
                System.out.println("Error response received from the API.");
            }
        } else {
            System.out.println("Failed to fetch jokes from the API.");
        }
        return jokes;
    }

    private List<String> getCategoriesFromApi() {
        final String uri = "https://v2.jokeapi.dev/categories";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JokeApiCategoriesResponse> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                JokeApiCategoriesResponse.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            JokeApiCategoriesResponse categoriesResponse = responseEntity.getBody();
            if (categoriesResponse != null && !categoriesResponse.error()) {
                return categoriesResponse.categories();
            } else {
                System.out.println("Error response received from the API.");
                System.out.println(categoriesResponse.error());
            }
        } else {
            System.out.println("Failed to fetch categories from the API.");
        }
        return Collections.emptyList();
    }
}
