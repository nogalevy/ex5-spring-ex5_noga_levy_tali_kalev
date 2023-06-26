package hac.utils;

import hac.beans.SearchFilter;
import hac.records.Joke;
import hac.records.JokeApiCategoriesResponse;
import hac.records.JokeApiResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JokeApiHandler {
    final static String API_DOMAIN_URL = "https://v2.jokeapi.dev/joke";
    final static String BLACKLIST_QUERY = "blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
    final static String CATEGORIES_URI = "https://v2.jokeapi.dev/categories";
    final static String GET_BY_ID_URI_QUERY = API_DOMAIN_URL + "/Any?idRange=";

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

    public static List<Joke> getJokesFromApi(SearchFilter sf){
        final String uri = getUri(sf);
        JokeApiResponse jokeApiResponse;
        List<Joke> jokes = Arrays.asList(new Joke());

        ResponseEntity<JokeApiResponse> responseEntity = GetRestExchange( uri, JokeApiResponse.class);

        if (responseEntity != null &&  responseEntity.getStatusCode().is2xxSuccessful()) {
            jokeApiResponse = responseEntity.getBody();
            System.out.println("jokeApiResponse: " + jokeApiResponse);
            if (jokeApiResponse != null && !jokeApiResponse.error()) {
                jokes = jokeApiResponse.jokes();
            }
        }
        return jokes;
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
//        Integer requestedId = id -1; //NOGA
        final String uri = GET_BY_ID_URI_QUERY + id;
        ResponseEntity<JokeApiResponse> responseEntity = GetRestExchange( uri, JokeApiResponse.class);

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            JokeApiResponse jokeApiResponse = responseEntity.getBody();
            if (jokeApiResponse != null && !jokeApiResponse.error()) {
                return jokeApiResponse.jokes().get(0);
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
        //NOGA: change to switch case?
        if (op == 1) {
            return opStr + "single&";
        }
        else if(op == 2){
            return opStr + "twopart&";
        }
        return "";
    }
}
