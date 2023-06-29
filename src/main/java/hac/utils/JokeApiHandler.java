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

/**
 * Joke api handler
 */
public class JokeApiHandler {
    private final static String API_DOMAIN_URL = "https://v2.jokeapi.dev/joke";
    private final static String BLACKLIST_QUERY = "blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
    private final static String CATEGORIES_URI = "https://v2.jokeapi.dev/categories";
    private final static String GET_BY_ID_URI_QUERY = API_DOMAIN_URL + "/Any?idRange=";

    /**
     * Get jokes from joke api
     * @param url String
     * @param resType Class<T>
     * @return ResponseEntity<T>
     * @param <T> T
     */
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

    /**
     * Gets user favourite jokes from joke api
     * @param favouritesList List of favourites
     * @return list of jokes
     */
    public static List<Joke> getUserFavouritesJokes(List<Favourite> favouritesList){
        ArrayList<Long> jokeIds = new ArrayList<Long>();
        for (Favourite fav : favouritesList) {
            jokeIds.add(fav.getJokeId());
        }
        return JokeApiHandler.getJokesByIdsFromApi(jokeIds);
    }

    /**
     * gets joke by uri
     * @param uri String
     * @return Joke
     */
    public static Joke getJokebyURI(String uri){
        ResponseEntity<Joke> responseEntity = GetRestExchange( uri, Joke.class);

        if (responseEntity != null &&  responseEntity.getStatusCode().is2xxSuccessful()) {
            Joke newJoke = responseEntity.getBody();
            if (newJoke != null && !newJoke.error()) {
                return newJoke;
            }
        }
        return null;
    }

    /**
     * Get joke from api based on search filer
     * @param sf SearchFilter
     * @return Joke
     */
    public static Joke getJokesFromApi(SearchFilter sf){
        final String uri = getUri(sf);

        Joke joke = getJokebyURI(uri);
        if (joke == null){
            joke = new Joke();
        }
        return joke;
    }

    /**
     * Get list of categories from joke api
     * @return List of strings
     */
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

    /**
     * Gets joke by ids from api
     * Note, there is no way to send a list of ids to ip api, so we have to send multiple requests
     * @param ids list of longs
     * @return list of jokes
     */
    public static List<Joke> getJokesByIdsFromApi(ArrayList<Long> ids){
        List<Joke> jokes = new ArrayList<Joke>();
        for (Long id : ids) {
            Joke joke = getJokeById(id);
            if(joke == null)continue;
            jokes.add(joke);
        }
        return jokes;
    }

    /**
     * Gets joke by id from api
     * @param id long
     * @return Joke
     */
    public static Joke getJokeById(Long id){
        final String uri = GET_BY_ID_URI_QUERY + id;

        return getJokebyURI(uri);
    }

    /**
     * Builds uri based on search filter
     * @param sf SearchFilter
     * @return String
     */
    public static String getUri(SearchFilter sf){
        String catgrs = buildCategoryQuery(sf.getSelectedCategories());
        String op = buildOptionsQuery(sf.getSelectedOption());
        return API_DOMAIN_URL + catgrs + op + BLACKLIST_QUERY;
    }

    /**
     * Builds category query
     * @param categories String[]
     * @return String
     */
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

    /**
     * Builds options query based on type of joke chosen
     * @param op int
     * @return String
     */
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
