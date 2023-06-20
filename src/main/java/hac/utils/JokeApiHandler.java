package hac.utils;

import hac.beans.SearchFilter;
import hac.records.Joke;
import hac.records.JokeApiCategoriesResponse;
import hac.records.JokeApiResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JokeApiHandler {
    final static String API_DOMAIN_URL = "https://v2.jokeapi.dev/joke";
    final static String BLACKLIST_QUERY = "blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
    final static String CATEGORIES_URI = "https://v2.jokeapi.dev/categories";
    final static String GET_BY_ID_URI_QUERY = API_DOMAIN_URL + "/Any?idRange=";

    private static  <T> ResponseEntity<T> GetRestExchange(String url, Class<T> resType){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                resType
        );
    }

    public static List<Joke> getJokesFromApi(SearchFilter sf){
        final String uri = getUri(sf);
        JokeApiResponse jokeApiResponse;
        List<Joke> jokes = null;

        ResponseEntity<JokeApiResponse> responseEntity = GetRestExchange( uri, JokeApiResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            jokeApiResponse = responseEntity.getBody();
            if (jokeApiResponse != null && !jokeApiResponse.error()) {
                jokes = jokeApiResponse.jokes();
                //NOGA: I deleted the for loop that use to be here long time ago i hope that okay. okay?
            } else {
                System.out.println("Error response received from the API.");
            }
        } else{
                System.out.println("Failed to fetch jokes from the API.");
        }
        return jokes;
    }

    public static List<String> getCategoriesFromApi() {
        ResponseEntity<JokeApiCategoriesResponse> responseEntity = GetRestExchange( CATEGORIES_URI, JokeApiCategoriesResponse.class);

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

    public static List<Joke> getJokesByIdsFromApi(ArrayList<Integer> ids){
        List<Joke> jokes = new ArrayList<Joke>();
        for (Integer id : ids) {
            Joke joke = getJokeById(id);
            jokes.add(joke);
        }
        return jokes;
    }

    public static Joke getJokeById(Integer id){
//        Integer requestedId = id -1; //NOGA
        final String uri = GET_BY_ID_URI_QUERY + id;
        ResponseEntity<JokeApiResponse> responseEntity = GetRestExchange( uri, JokeApiResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            JokeApiResponse jokeApiResponse = responseEntity.getBody();
            if (jokeApiResponse != null && !jokeApiResponse.error()) {
                return jokeApiResponse.jokes().get(0);
            } else {
                System.out.println("Error response received from the API.");
                System.out.println(jokeApiResponse.error());
            }
        } else {
            System.out.println("Failed to fetch by id from the API.");
        }
        return null;
    }

    public static String getUri(SearchFilter sf){
        String catgrs = buildCategoryQuery(sf.getSelectedCategories());
        String op = buildOptionsQuery(sf.getSelectedOption());
        return API_DOMAIN_URL + catgrs + op + BLACKLIST_QUERY;
    }

    private static String buildCategoryQuery(String[] categories){
        String query = "/";
        if(categories.length == 0){query = "/Any";}
        for ( int i = 0 ; i < categories.length; i++) {
            String c = categories[i];
            query = query.concat(c);
            if(i < categories.length - 1) query = query + ",";
        }
        return query + '?';
    }

    private static String buildOptionsQuery(int op){
        String opStr = "type=";

        if (op == 1) {
            return opStr + "single&";
        }
        else if(op == 2){
            return opStr + "twopart&";
        }
        return "";
    }
}
