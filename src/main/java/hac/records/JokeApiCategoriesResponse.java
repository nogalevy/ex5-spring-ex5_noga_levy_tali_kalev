package hac.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Joke API categories response
 * @param error boolean
 * @param categories list of categories
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record JokeApiCategoriesResponse(boolean error, List<String> categories) {
}
