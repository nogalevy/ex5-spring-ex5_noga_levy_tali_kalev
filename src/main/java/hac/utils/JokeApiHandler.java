package hac.utils;

import hac.beans.SearchFilter;
import hac.records.Joke;
import hac.records.JokeApiCategoriesResponse;
import hac.repo.Favourite;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JokeApiHandler {
    private final static String API_DOMAIN_URL = "https://v2.jokeapi.dev/joke";
    private final static String BLACKLIST_QUERY = "blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
    private final static String CATEGORIES_URI = "https://v2.jokeapi.dev/categories";
    private final static String GET_BY_ID_URI_QUERY = API_DOMAIN_URL + "/Any?idRange=";

    private static  <T> ResponseEntity<T> GetRestExchange(String url, Class<T> resType){
        RestTemplate restTemplate = new RestTemplate();
        try{

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                resType
        );
        }
        catch (Exception err){
            return null;
        }
    }

    public static List<Joke> getUserFavouritesJokes(List<Favourite> favouritesList){
        ArrayList<Long> jokeIds = new ArrayList<Long>();
        for (Favourite fav : favouritesList) {
            jokeIds.add(fav.getJokeId());
        }
        return JokeApiHandler.getJokesByIdsFromApi(jokeIds);
    }

    public static Joke getJokesFromApi(SearchFilter sf){
        final String uri = getUri(sf);

        ResponseEntity<Joke> responseEntity = GetRestExchange( uri, Joke.class);

        if (responseEntity != null &&  responseEntity.getStatusCode().is2xxSuccessful()) {
            Joke newJoke = responseEntity.getBody();
            if (newJoke != null && !newJoke.error()) {
                return newJoke;
            }
        }
        return new Joke();
    }

    public static List<String> getCategoriesFromApi() {
        ResponseEntity<JokeApiCategoriesResponse> responseEntity = GetRestExchange( CATEGORIES_URI, JokeApiCategoriesResponse.class);

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            JokeApiCategoriesResponse categoriesResponse = responseEntity.getBody();
            if (categoriesResponse != null && !categoriesResponse.error()) {
                return categoriesResponse.categories();
            }
        }
        return Collections.emptyList();
    }

    public static List<Joke> getJokesByIdsFromApi(ArrayList<Long> ids){
        List<Joke> jokes = new ArrayList<Joke>();
        for (Long id : ids) {
            Joke joke = getJokeById(id);
            if(joke == null)continue;
            jokes.add(joke);
        }
        return jokes;
    }

    public static Joke getJokeById(Long id){
        final String uri = GET_BY_ID_URI_QUERY + id;
        ResponseEntity<Joke> responseEntity = GetRestExchange( uri, Joke.class);

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            Joke newJoke = responseEntity.getBody();
            if (newJoke != null && !newJoke.error()) {
                return newJoke;
            }
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
