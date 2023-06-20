package hac.beans;

import hac.records.JokeApiCategoriesResponse;
import hac.records.JokeApiResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import hac.records.Joke;

//TODO : change class name
public class JokesList {
    private ArrayList<JokeBean> jokesList = new ArrayList<>();

    public JokesList() {
    }



    //============================GET FROM API========================================
    public static List<Joke> getJokesFromApi(SearchFilter s){//String uri){

        final String uri = getUri(s);
        //        final String uri = "https://v2.jokeapi.dev/joke/Any?amount=4?format=json";
        //        final String uri = "https://v2.jokeapi.dev/joke/Any";
        //        final String uri = "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&amount=2";
//        final String uri = "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
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
                    System.out.println("here............");
                    for (hac.records.Joke joke : jokes) {
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

    public static List<String> getCategoriesFromApi() {
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


    public ArrayList<JokeBean> getJokesList() {
        return jokesList;
    }

    public void setJokesList(ArrayList<JokeBean> jokes) {
        this.jokesList = jokes;
    }

    public void add (JokeBean joke) {
        jokesList.add(joke);
    }

    public void clear() {
        jokesList.clear();
    }

    //TODO : clean this function or something
    public static String getUri(SearchFilter s){
        String BLACKLIST = "?blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
//        SearchFilter s = new SearchFilter();
        String u = "https://v2.jokeapi.dev/joke";
        String catgrs = "/";

        int opInt = s.getSelectedOption();
        String op = "?type=";

        if(opInt == 0) op = "";
        else if (opInt == 1) {
            op = op + "single";
        }
        else{
            op = op + "twopart";
        }

        if(s.getSelectedCategories().length == 0){catgrs = "/Any";}
        for ( int i = 0 ; i < s.getSelectedCategories().length; i++) {
            String c = s.getSelectedCategories()[i];
            catgrs = catgrs.concat(c);
            if(i < s.getSelectedCategories().length - 1) catgrs = catgrs + ",";
        }
        String x = u + catgrs + op + BLACKLIST;
        System.out.println("---- uri: ----" + x);

        return x;
    }
}
