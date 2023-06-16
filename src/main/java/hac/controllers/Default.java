package hac.controllers;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import java.util.List;

/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class Default {

    @GetMapping("/")
    public String index(Model model) {
//        model.addAttribute("greeting", "Hello World");
        List<Joke> jokes = getJokesFromApi();
        String joke = jokes.get(0).joke()==null ? jokes.get(0).setup() + '\n' + jokes.get(0).delivery()  : jokes.get(0).joke();
        model.addAttribute("joke", joke);
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
    private @ResponseBody Boolean getJokes()
    {
//        List<Joke> jokes = getJokesFromApi();
        return true;
    }

    private List<Joke> getJokesFromApi(){
        final String uri = "https://v2.jokeapi.dev/joke/Any?amount=4?format=json";
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
}
